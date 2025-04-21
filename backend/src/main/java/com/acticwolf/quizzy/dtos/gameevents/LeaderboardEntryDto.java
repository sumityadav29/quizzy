package com.acticwolf.quizzy.dtos.gameevents;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryDto {

    private String nickname;
    private int score;

}