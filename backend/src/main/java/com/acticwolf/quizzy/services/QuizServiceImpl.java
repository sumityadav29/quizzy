package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.AddQuestionRequestDto;
import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuestionResponseDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.QuestionRepository;
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
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public QuizDetailResponseDto createQuiz(CreateQuizRequestDto request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCreatedBy(request.getCreatedBy());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Quiz savedQuiz = quizRepository.save(quiz);

        return getQuizDetailById(savedQuiz.getId());
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

    @Override
    public QuizDetailResponseDto addQuestionToQuiz(Integer quizId, AddQuestionRequestDto request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id : " + quizId));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(request.getQuestionText());
        question.setCorrectOption(request.getCorrectOption());
        question.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        try {
            question.setOptionsJson(objectMapper.writeValueAsString(request.getOptions()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert options to JSON", e);
        }

        Question savedQuestion = questionRepository.save(question);

        return getQuizDetailById(quizId);
    }

    private List<String> parseJsonArray(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse options JSON", e);
        }
    }
}