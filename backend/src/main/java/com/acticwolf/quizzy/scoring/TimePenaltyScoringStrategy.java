package com.acticwolf.quizzy.scoring;

import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.models.Player;
import org.springframework.stereotype.Component;

@Component
public class TimePenaltyScoringStrategy implements ScoringStrategy {

    private static final int BASE_SCORE = 500;
    private static final int PENALTY_PER_SECOND = 2;

    @Override
    public int calculateScore(GameSession session, Question question, Player player, boolean isCorrect, int responseTimeMs) {
        if (!isCorrect) return 0;

        int seconds = responseTimeMs / 1000;
        int penalty = seconds * PENALTY_PER_SECOND;

        return Math.max(0, BASE_SCORE - penalty);
    }
}