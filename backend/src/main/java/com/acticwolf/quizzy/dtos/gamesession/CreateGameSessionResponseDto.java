package com.acticwolf.quizzy.dtos.gamesession;

import com.acticwolf.quizzy.models.GameSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameSessionResponseDto {
    private Integer id;
    private String roomCode;
    private GameSession.SessionStatus status;
    private Timestamp createdAt;
}