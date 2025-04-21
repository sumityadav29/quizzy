package com.acticwolf.quizzy.dtos.gamesession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameSessionRequestDto {
    private Integer quizId;
    private Integer roundTime;
    private Integer roundCooldownTime;
}