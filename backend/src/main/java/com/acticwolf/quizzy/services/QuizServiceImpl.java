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
                .map(q -> QuestionResponseDto.builder()
                        .id(q.getId())
                        .questionText(q.getQuestionText())
                        .options(parseJsonArray(q.getOptionsJson()))
                        .correctOption(q.getCorrectOption())
                        .createdAt(q.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return QuizDetailResponseDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .createdBy(quiz.getCreatedBy())
                .questions(questionResponseDtos)
                .createdAt(quiz.getCreatedAt())
                .build();
    }

    private List<String> parseJsonArray(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse options JSON", e);
        }
    }
}