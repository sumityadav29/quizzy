package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.CreateGameSessionRequestDto;
import com.acticwolf.quizzy.dtos.CreateGameSessionResponseDto;

public interface GameSessionService {

    CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto);

}