package com.acticwolf.quizzy.services.gameevents;

import com.acticwolf.quizzy.dtos.gameevents.LeaderboardEntryDto;
import com.acticwolf.quizzy.dtos.gameevents.LiveQuestionResponseDto;

import java.util.List;

public interface GameEventsRealTimeService {

    public LiveQuestionResponseDto sendNextQuestion(Integer sessionId);

    List<LeaderboardEntryDto> getLeaderboard(Integer sessionId);

    public void sendLeaderBoardToSession(Integer sessionId);

}
