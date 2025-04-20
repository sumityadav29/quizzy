package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;

public interface QuizService {
    QuizDetailResponseDto createQuiz(CreateQuizRequestDto request);
    QuizDetailResponseDto getQuizById(Integer id);
}