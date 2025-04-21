package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.dtos.JoinGameSessionResponseDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Player;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.GameSessionRepository;
import com.acticwolf.quizzy.repositories.PlayerRepository;
import com.acticwolf.quizzy.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final QuizRepository quizRepository;
    private final PlayerRepository playerRepository;
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
