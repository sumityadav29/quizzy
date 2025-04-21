package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.LeaderboardEntryDto;
import com.acticwolf.quizzy.dtos.LiveQuestionResponseDto;
import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameSessionRealTimeServiceImpl implements GameSessionRealTimeService {

    private final GameSseService gameSseService;
    private final ObjectMapper objectMapper;
    private final QuestionRepository questionRepository;
    private final GameSessionRepository gameSessionRepository;

    public enum GameEvent {
        QUIZ_STARTED,
        NEXT_QUESTION,
        ROUND_TIME_UP,
        QUIZ_ENDED
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

            gameSseService.sendToSession(sessionId, GameEvent.QUIZ_ENDED, Map.of());
            return null;
        }

        Question nextQuestion = questions.get(nextIndex);
        session.setCurrentQuestion(nextQuestion);
        gameSessionRepository.save(session);

        LiveQuestionResponseDto dto = LiveQuestionResponseDto.builder()
                .id(nextQuestion.getId())
                .questionText(nextQuestion.getQuestionText())
                .options(parseJsonArray(nextQuestion.getOptionsJson()))
                .maximumAllowedTime(session.getRoundTime())
                .build();

        gameSseService.sendToSession(sessionId, GameEvent.NEXT_QUESTION, dto);

        return dto;
    }

    @Override
    public List<LeaderboardEntryDto> getLeaderboard(Integer sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found"));

        return session.getPlayers().stream()
                .map(player -> new LeaderboardEntryDto(player.getNickname(), player.getScore()))
                .sorted(Comparator.comparingInt(LeaderboardEntryDto::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void sendLeaderBoardToSession(Integer sessionId) {
        List<LeaderboardEntryDto> leaderboard = getLeaderboard(sessionId);

        gameSseService.sendToSession(sessionId, GameEvent.ROUND_TIME_UP, leaderboard);
    }

    private int findQuestionIndex(List<Question> questions, int currentId) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(currentId)) {
                return i;
            }
        }
        return -1;
    }

    public List<String> parseJsonArray(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse options JSON", e);
        }
    }

}
