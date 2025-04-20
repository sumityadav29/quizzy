package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuestionResponseDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.QuizRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;

    @Override
    public QuizDetailResponseDto createQuiz(CreateQuizRequestDto request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCreatedBy(request.getCreatedBy());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Quiz saved = quizRepository.save(quiz);

        return getQuizDetailById(saved.getId());
    }

    @Override
    public QuizDetailResponseDto getQuizDetailById(Integer id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id : " + id));

        List<QuestionResponseDto> questionResponseDtos = quiz.getQuestions()
                .stream()
                .map(q -> new QuestionResponseDto(
                        q.getId(),
                        q.getQuestionText(),
                        parseJsonArray(q.getOptionsJson()),
                        q.getCorrectOption(),
                        q.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new QuizDetailResponseDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getCreatedBy(),
                quiz.getCreatedAt(),
                questionResponseDtos
        );
    }

    private List<String> parseJsonArray(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse options JSON", e);
        }
    }
}