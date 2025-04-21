package com.acticwolf.quizzy.services;

import com.acticwolf.quizzy.dtos.LeaderboardEntryDto;
import com.acticwolf.quizzy.dtos.LiveQuestionResponseDto;

import java.util.List;

public interface GameSessionRealTimeService {

    public LiveQuestionResponseDto sendNextQuestion(Integer sessionId);

    List<LeaderboardEntryDto> getLeaderboard(Integer sessionId);

    public void sendLeaderBoardToSession(Integer sessionId);

}
