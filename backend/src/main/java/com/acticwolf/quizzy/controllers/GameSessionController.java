package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.dtos.JoinGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.JoinGameSessionResponseDto;
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

}