package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.quiz.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.quiz.QuizDetailResponseDto;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.QuizRepository;
import com.acticwolf.quizzy.services.quiz.QuizServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @InjectMocks
    private QuizServiceImpl quizService;

    @Mock
    private QuizRepository quizRepository;

    @Test
    void testCreateQuiz_success() {
        CreateQuizRequestDto dto = CreateQuizRequestDto.builder()
                .title("Java Basics")
                .description("A quiz on Java")
                .createdBy("admin")
                .build();

        Quiz savedQuiz = new Quiz(1, dto.getTitle(), dto.getDescription(), dto.getCreatedBy(), null, new ArrayList<>());
        Mockito.when(quizRepository.save(ArgumentMatchers.any())).thenReturn(savedQuiz);

        QuizDetailResponseDto result = quizService.createQuiz(dto);

        Assertions.assertEquals(dto.getTitle(), result.getTitle());
        Assertions.assertEquals(1, result.getId());
        Mockito.verify(quizRepository).save(ArgumentMatchers.any());
    }

    @Test
    void testGetQuiz_success() {
        Quiz quiz = new Quiz(1, "Java", "Desc", "admin", null, new ArrayList<>());
        Mockito.when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));

        QuizDetailResponseDto result = quizService.getQuizDetailById(1);
        Assertions.assertEquals("Java", result.getTitle());
    }
}