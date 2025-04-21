package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.*;

public interface GameSessionService {

    CreateGameSessionResponseDto createSession(CreateGameSessionRequestDto requestDto);

    JoinGameSessionResponseDto joinSessionByRoomCode(String roomCode, String nickname);

    void startSession(Integer sessionId);

    LiveQuestionResponseDto sendNextQuestion(Integer sessionId);

    SubmitAnswerResponseDto submitAnswer(Integer sessionId, Integer questionId, SubmitAnswerRequestDto requestDto);

}