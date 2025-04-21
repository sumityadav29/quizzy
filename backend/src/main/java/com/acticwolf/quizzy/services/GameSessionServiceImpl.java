package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.GameSessionRepository;
import com.acticwolf.quizzy.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final QuizRepository quizRepository;
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
