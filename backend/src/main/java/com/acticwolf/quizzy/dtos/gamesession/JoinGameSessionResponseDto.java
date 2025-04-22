package com.acticwolf.quizzy.dtos.gamesession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameSessionResponseDto {
    private Integer playerId;
    private String playerToken;
    private String nickname;
    private Integer gameSessionId;
}