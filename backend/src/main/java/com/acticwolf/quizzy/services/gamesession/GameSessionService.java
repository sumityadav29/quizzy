package com.acticwolf.quizzy.services.gamesession;

import com.acticwolf.quizzy.dtos.gamesession.*;

public interface GameSessionService {

    CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto);

    JoinGameSessionResponseDto joinSessionByRoomCode(String roomCode, String nickname);

    void startSession(Integer sessionId);

    SubmitAnswerResponseDto submitAnswer(Integer sessionId, Integer questionId, SubmitAnswerRequestDto requestDto);

}