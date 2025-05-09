package com.acticwolf.quizzy.services.gamesession;

import com.acticwolf.quizzy.dtos.gameevents.LeaderboardEntryDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.*;
import com.acticwolf.quizzy.services.gameevents.GameEventsRealTimeService;
import com.acticwolf.quizzy.services.gameevents.GameEventsRealTimeServiceImpl;
import com.acticwolf.quizzy.services.gameevents.GameSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameSessionExecutorServiceImpl implements GameSessionExecutorService {

    private final GameSseService gameSseService;
    private final GameSessionRepository gameSessionRepository;
    private final GameEventsRealTimeService gameEventsRealTimeService;

    @Async
    public void runQuizSession(Integer sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setStatus(GameSession.SessionStatus.IN_PROGRESS);
        session.setStartedAt(new Timestamp(System.currentTimeMillis()));
        gameSessionRepository.save(session);
        gameSseService.sendToSession(session.getId(), GameEventsRealTimeServiceImpl.GameEvent.QUIZ_STARTED, Map.of());

        Quiz quiz = session.getQuiz();
        List<Question> questions = quiz.getQuestions();
        int roundTime = session.getRoundTime();
        int cooldownTime = session.getRoundCooldownTime();

        for (Question question : questions) {
            if (question == null) continue;

            GameSession sessionLocal = gameSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            sessionLocal.setStatus(GameSession.SessionStatus.IN_PROGRESS);
            gameSessionRepository.save(sessionLocal);

            gameEventsRealTimeService.sendNextQuestion(sessionLocal.getId());
            sleepSeconds(roundTime);

            sessionLocal = gameSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            sessionLocal.setStatus(GameSession.SessionStatus.SHOWING_LEADERBOARD);
            gameSessionRepository.save(sessionLocal);

            gameEventsRealTimeService.sendLeaderBoardToSession(sessionLocal.getId());
            sleepSeconds(cooldownTime);
        }

        session.setStatus(GameSession.SessionStatus.FINISHED);
        gameSessionRepository.save(session);

        List<LeaderboardEntryDto> leaderboard = gameEventsRealTimeService.getLeaderboard(session.getId());
        gameSseService.sendToSession(session.getId(), GameEventsRealTimeServiceImpl.GameEvent.QUIZ_ENDED, leaderboard);
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
