package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.*;

import java.util.List;

public interface GameSessionService {

    CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto);

    JoinGameSessionResponseDto joinSessionByRoomCode(String roomCode, String nickname);

    void startSession(Integer sessionId);

    SubmitAnswerResponseDto submitAnswer(Integer sessionId, Integer questionId, SubmitAnswerRequestDto requestDto);

}