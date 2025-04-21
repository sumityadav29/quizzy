package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.dtos.*;
import com.acticwolf.quizzy.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDetailResponseDto> createQuiz(@RequestBody CreateQuizRequestDto request) {
        QuizDetailResponseDto response = quizService.createQuiz(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDetailResponseDto> getQuiz(@PathVariable Integer id) {
        QuizDetailResponseDto response = quizService.getQuizDetailById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<QuizDetailResponseDto> addQuestion(
            @PathVariable Integer quizId,
            @RequestBody AddQuestionRequestDto requestDto) {
        QuizDetailResponseDto responseDto = quizService.addQuestionToQuiz(quizId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginatedQuizResponseDto> getAllQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedQuizResponseDto response = quizService.getAllQuizzes(page, size);
        return ResponseEntity.ok(response);
    }
}