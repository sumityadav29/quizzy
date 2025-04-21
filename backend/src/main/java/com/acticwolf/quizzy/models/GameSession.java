package com.acticwolf.quizzy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @OneToOne
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    private Timestamp startedAt;

    private Timestamp endedAt;

    private Timestamp createdAt;

    @Column(unique = true, length = 6)
    private String roomCode;

    @OneToOne
    @JoinColumn(name = "winner")
    private Player winner;

    @Column(name = "round_time")
    private Integer roundTime = 30;

    @Column(name = "round_cooldown_time")
    private Integer roundCooldownTime = 10;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    public enum SessionStatus {
        WAITING,
        IN_PROGRESS,
        SHOWING_LEADERBOARD,
        FINISHED
    }
}