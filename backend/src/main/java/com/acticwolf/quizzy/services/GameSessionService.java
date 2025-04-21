package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;
import com.acticwolf.quizzy.dtos.JoinGameSessionResponseDto;

public interface GameSessionService {

    CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto);

    JoinGameSessionResponseDto joinSessionByRoomCode(String roomCode, String nickname);

    void startSession(Integer sessionId);

}