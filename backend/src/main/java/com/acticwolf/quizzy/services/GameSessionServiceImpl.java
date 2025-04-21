package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.dtos.JoinGameSessionResponseDto;
import com.acticwolf.quizzy.dtos.LiveQuestionResponseDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Player;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.GameSessionRepository;
import com.acticwolf.quizzy.repositories.PlayerRepository;
import com.acticwolf.quizzy.repositories.QuestionRepository;
import com.acticwolf.quizzy.repositories.QuizRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final SseService sseService;
    private final ObjectMapper objectMapper;
    private final QuizRepository quizRepository;
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;
    private final GameSessionRepository gameSessionRepository;

    @Override
    public CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(requestDto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id : " + requestDto.getQuizId()));

        String roomCode = generateRoomCode();

        GameSession session = new GameSession();
        session.setQuiz(quiz);
        session.setRoomCode(roomCode);
        session.setStatus(GameSession.SessionStatus.WAITING);
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
                .build();
    }

    @Override
    public void startSession(Integer sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != GameSession.SessionStatus.WAITING) {
            throw new IllegalStateException("Only sessions in WAITING state can be started");
        }

        session.setStatus(GameSession.SessionStatus.IN_PROGRESS);
        session.setStartedAt(new Timestamp(System.currentTimeMillis()));
        session.setCurrentQuestion(null);
        gameSessionRepository.save(session);

        sseService.sendToSession(sessionId, "QUIZ_STARTED", Map.of());
    }

    @Override
    public LiveQuestionResponseDto sendNextQuestion(Integer sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != GameSession.SessionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Session is not in progress.");
        }

        List<Question> questions = questionRepository.findByQuizId(session.getQuiz().getId());
        questions.sort(Comparator.comparingInt(Question::getId));

        int nextIndex = 0;

        if (session.getCurrentQuestion() != null) {
            int currentIndex = findQuestionIndex(questions, session.getCurrentQuestion().getId());
            nextIndex = currentIndex + 1;
        }

        if (nextIndex >= questions.size()) {
            session.setStatus(GameSession.SessionStatus.FINISHED);
            session.setEndedAt(new Timestamp(System.currentTimeMillis()));
            session.setCurrentQuestion(null);
            gameSessionRepository.save(session);

            sseService.sendToSession(sessionId, "QUIZ_ENDED", Map.of());
            return null;
        }

        Question nextQuestion = questions.get(nextIndex);
        session.setCurrentQuestion(nextQuestion);
        gameSessionRepository.save(session);

        LiveQuestionResponseDto dto = LiveQuestionResponseDto.builder()
                .id(nextQuestion.getId())
                .questionText(nextQuestion.getQuestionText())
                .options(parseJsonArray(nextQuestion.getOptionsJson()))
                .build();

        sseService.sendToSession(sessionId, "NEXT_QUESTION", dto);

        return dto;
    }

    private int findQuestionIndex(List<Question> questions, int currentId) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(currentId)) {
                return i;
            }
        }
        return -1;
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

    public List<String> parseJsonArray(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse options JSON", e);
        }
    }

}
