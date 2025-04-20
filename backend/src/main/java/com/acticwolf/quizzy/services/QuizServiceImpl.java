package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;
import com.acticwolf.quizzy.models.Quiz;
import com.acticwolf.quizzy.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    @Override
    public QuizDetailResponseDto createQuiz(CreateQuizRequestDto request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCreatedBy(request.getCreatedBy());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Quiz saved = quizRepository.save(quiz);

        return new QuizDetailResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getCreatedBy(),
                saved.getCreatedAt()
        );
    }

    @Override
    public QuizDetailResponseDto getQuizById(Integer id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return new QuizDetailResponseDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getCreatedBy(),
                quiz.getCreatedAt()
        );
    }
}