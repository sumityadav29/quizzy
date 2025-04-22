package com.acticwolf.quizzy.services.gamesession;

import com.acticwolf.quizzy.dtos.gamesession.*;
import com.acticwolf.quizzy.models.*;
import com.acticwolf.quizzy.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final QuizRepository quizRepository;
    private final PlayerRepository playerRepository;
    private final GameSessionRepository gameSessionRepository;
    private final GameSessionExecutorService gameSessionExecutorService;
    private final GameSessionAnswerRepository gameSessionAnswerRepository;

    @Override
    public CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(requestDto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id : " + requestDto.getQuizId()));

        String roomCode = generateRoomCode();

        GameSession session = new GameSession();
        session.setQuiz(quiz);
        session.setRoomCode(roomCode);
        session.setStatus(GameSession.SessionStatus.WAITING);
        session.setRoundTime(Optional.ofNullable(requestDto.getRoundTime()).orElse(30));
        session.setRoundCooldownTime(Optional.ofNullable(requestDto.getRoundCooldownTime()).orElse(10));
        session.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        GameSession saved = gameSessionRepository.save(session);

        return CreateGameSessionResponseDto.builder()
                .id(saved.getId())
                .roomCode(saved.getRoomCode())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public JoinGameSessionResponseDto joinSessionByRoomCode(String roomCode, String nickname) {
        GameSession session = gameSessionRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Optional<Player> existingPlayer = playerRepository.findBySessionId(session.getId()).stream()
                .filter(p -> p.getNickname().equalsIgnoreCase(nickname))
                .findFirst();

        Player player = existingPlayer.orElseGet(() -> {
            Player newPlayer = new Player();
            newPlayer.setSession(session);
            newPlayer.setNickname(nickname);
            newPlayer.setJoinedAt(new Timestamp(System.currentTimeMillis()));
            newPlayer.setPlayerToken(UUID.randomUUID().toString());
            return playerRepository.save(newPlayer);
        });

        return JoinGameSessionResponseDto.builder()
                .playerId(player.getId())
                .playerToken(player.getPlayerToken())
                .nickname(player.getNickname())
                .gameSessionId(session.getId())
                .build();
    }

    @Override
    public void startSession(Integer sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != GameSession.SessionStatus.WAITING) {
            throw new IllegalStateException("Only sessions in WAITING state can be started");
        }

        gameSessionExecutorService.runQuizSession(sessionId);
    }

    @Override
    public SubmitAnswerResponseDto submitAnswer(Integer sessionId, Integer questionId, SubmitAnswerRequestDto requestDto) {
        System.out.println("submitAnswer called with sessionId = " + sessionId + ", questionId = " + questionId);
        System.out.println("Player token = " + requestDto.getPlayerToken() + ", selectedIndex = " + requestDto.getSelectedIndex());

        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != GameSession.SessionStatus.IN_PROGRESS) {
            throw new RuntimeException("Session is not active.");
        }

        Question currentQuestion = session.getCurrentQuestion();
        if (currentQuestion == null) {
            throw new RuntimeException("No current question.");
        }

        if (!questionId.equals(currentQuestion.getId())) {
            throw new RuntimeException("This is not the live question in game.");
        }

        Player player = playerRepository.findByPlayerToken(requestDto.getPlayerToken())
                .orElseThrow(() -> new RuntimeException("Invalid player token"));

        if (!player.getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Player is not part of this session.");
        }

        Optional<GameSessionAnswer> existing = gameSessionAnswerRepository
                .findByPlayerIdAndQuestionId(player.getId(), currentQuestion.getId());

        if (existing.isPresent()) {
            throw new RuntimeException("Answer already submitted.");
        }

        boolean isCorrect = requestDto.getSelectedIndex().equals(currentQuestion.getCorrectOption());

        GameSessionAnswer answer = new GameSessionAnswer();
        answer.setPlayer(player);
        answer.setQuestion(currentQuestion);
        answer.setSelectedIndex(requestDto.getSelectedIndex());
        answer.setIsCorrect(isCorrect);
        answer.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        long elapsedTime = System.currentTimeMillis() - session.getStartedAt().getTime();
        int responseTime = (int) Math.min(elapsedTime, Integer.MAX_VALUE);
        answer.setResponseTime(responseTime);

        int baseScore = 500;
        int secondsTaken = responseTime / 1000;
        int penalty = secondsTaken * 2;

        int scoreForCurrentQuestion = isCorrect ? Math.max(0, baseScore - penalty) : 0;
        answer.setScore(scoreForCurrentQuestion);

        System.out.println("Scoring debug:");
        System.out.println("Elapsed time (ms): " + responseTime);
        System.out.println("Seconds taken: " + secondsTaken);
        System.out.println("Penalty: " + penalty);
        System.out.println("Final score: " + scoreForCurrentQuestion);

        gameSessionAnswerRepository.save(answer);

        int newPlayerScore = player.getScore() + scoreForCurrentQuestion;
        player.setScore(newPlayerScore);
        playerRepository.save(player);

        System.out.println("Updated player score: " + newPlayerScore);

        return SubmitAnswerResponseDto.builder()
                .correct(isCorrect)
                .responseTime(responseTime)
                .build();
    }

    private String generateRoomCode() {
        int length = 6;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
