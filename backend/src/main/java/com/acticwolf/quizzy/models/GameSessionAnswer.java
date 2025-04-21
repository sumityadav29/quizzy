package com.acticwolf.quizzy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "game_session_answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "question_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSessionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer selectedIndex;

    private Timestamp submittedAt;

    private Boolean isCorrect;

    private Integer responseTime;

    private Integer score = 0;
}
