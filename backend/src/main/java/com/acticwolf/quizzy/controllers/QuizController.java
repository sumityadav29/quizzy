package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.dtos.CreateQuizRequestDto;
import com.acticwolf.quizzy.dtos.QuizDetailResponseDto;
import com.acticwolf.quizzy.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    @Autowired
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDetailResponseDto> createQuiz(@RequestBody CreateQuizRequestDto request) {
        QuizDetailResponseDto response = quizService.createQuiz(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDetailResponseDto> getQuiz(@PathVariable Integer id) {
        QuizDetailResponseDto response = quizService.getQuizById(id);
        return ResponseEntity.ok(response);
    }
}