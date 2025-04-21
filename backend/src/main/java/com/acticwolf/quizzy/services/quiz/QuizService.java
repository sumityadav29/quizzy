package com.acticwolf.quizzy.services.quiz;

import com.acticwolf.quizzy.dtos.quiz.AddQuestionRequestDto;
import com.acticwolf.quizzy.dtos.quiz.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.quiz.PaginatedQuizResponseDto;
import com.acticwolf.quizzy.dtos.quiz.QuizDetailResponseDto;

public interface QuizService {
    QuizDetailResponseDto createQuiz(CreateQuizRequestDto request);

    QuizDetailResponseDto getQuizDetailById(Integer id);

    QuizDetailResponseDto addQuestionToQuiz(Integer quizId, AddQuestionRequestDto request);

    PaginatedQuizResponseDto getAllQuizzes(int page, int size);
}