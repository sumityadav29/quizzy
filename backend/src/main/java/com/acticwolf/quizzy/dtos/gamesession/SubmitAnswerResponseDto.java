package com.acticwolf.quizzy.dtos.gamesession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerResponseDto {
    private boolean correct;
    private long responseTime;
}