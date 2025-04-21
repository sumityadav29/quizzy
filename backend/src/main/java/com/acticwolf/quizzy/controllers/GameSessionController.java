package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.dtos.*;
import com.acticwolf.quizzy.services.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping
    public ResponseEntity<CreateGameSessionResponseDto> createSession(
            @RequestBody CreateGameSessionRequestDto requestDto) {
        CreateGameSessionResponseDto response = gameSessionService.createSession(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/code/{roomCode}/join")
    public ResponseEntity<JoinGameSessionResponseDto> joinSessionByRoomCode(
            @PathVariable String roomCode,
            @RequestBody JoinGameSessionRequestDto requestDto) {
        JoinGameSessionResponseDto response = gameSessionService.joinSessionByRoomCode(roomCode, requestDto.getNickname());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startSession(@PathVariable Integer id) {
        gameSessionService.startSession(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/questions/next")
    public ResponseEntity<LiveQuestionResponseDto> sendNextQuestion(@PathVariable Integer id) {
        LiveQuestionResponseDto dto = gameSessionService.sendNextQuestion(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

}