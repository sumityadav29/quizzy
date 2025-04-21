package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.AddQuestionRequestDto;
import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuestionResponseDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;

public interface QuizService {
    QuizDetailResponseDto createQuiz(CreateQuizRequestDto request);
    QuizDetailResponseDto getQuizDetailById(Integer id);

    QuizDetailResponseDto addQuestionToQuiz(Integer quizId, AddQuestionRequestDto request);
}