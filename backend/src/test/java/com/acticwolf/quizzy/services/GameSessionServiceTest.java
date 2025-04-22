package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.gamesession.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.gamesession.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.GameSessionRepository;
import com.acticwolf.quizzy.repositories.QuizRepository;
import com.acticwolf.quizzy.services.gamesession.GameSessionExecutorService;
import com.acticwolf.quizzy.services.gamesession.GameSessionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameSessionServiceTest {

    @InjectMocks
    private GameSessionServiceImpl service;

    @Mock
    private GameSessionRepository repo;

    @Mock
    private QuizRepository quizRepo;

    @Mock
    private GameSessionExecutorService executor;

    @Test
    void testCreateSession_success() {
        Quiz quiz = new Quiz(); quiz.setId(1);
        Mockito.when(quizRepo.findById(1)).thenReturn(Optional.of(quiz));

        GameSession saved = new GameSession(); saved.setId(1); saved.setQuiz(quiz);
        Mockito.when(repo.save(ArgumentMatchers.any())).thenReturn(saved);

        CreateGameSessionRequestDto dto = new CreateGameSessionRequestDto();
        dto.setQuizId(1);
        CreateGameSessionResponseDto result = service.createSession(dto);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(quiz.getId(), result.getId());
    }

    @Test
    void testStartSession_invalidState_throws() {
        GameSession session = new GameSession();
        session.setId(1);
        session.setStatus(GameSession.SessionStatus.IN_PROGRESS);

        Mockito.when(repo.findById(1)).thenReturn(Optional.of(session));

        Assertions.assertThrows(IllegalStateException.class, () -> service.startSession(1));
    }
}