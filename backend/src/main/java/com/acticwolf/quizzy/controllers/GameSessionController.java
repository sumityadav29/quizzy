package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.dtos.*;
import com.acticwolf.quizzy.services.GameSessionRealTimeService;
import com.acticwolf.quizzy.services.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;
    private final GameSessionRealTimeService gameSessionRealTimeService;

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
        LiveQuestionResponseDto dto = gameSessionRealTimeService.sendNextQuestion(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/questions/{questionId}/answer")
    public ResponseEntity<SubmitAnswerResponseDto> submitAnswer(
            @PathVariable Integer id,
            @PathVariable Integer questionId,
            @RequestBody SubmitAnswerRequestDto requestDto) {
        SubmitAnswerResponseDto response = gameSessionService.submitAnswer(id, questionId, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/leaderboard")
    public List<LeaderboardEntryDto> getLeaderboard(@PathVariable Integer id) {
        return gameSessionRealTimeService.getLeaderboard(id);
    }

}