package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.*;

public interface QuizService {
    QuizDetailResponseDto createQuiz(CreateQuizRequestDto request);

    QuizDetailResponseDto getQuizDetailById(Integer id);

    QuizDetailResponseDto addQuestionToQuiz(Integer quizId, AddQuestionRequestDto request);

    PaginatedQuizResponseDto getAllQuizzes(int page, int size);
}