package com.acticwolf.quizzy.scoring;

import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Question;
import com.acticwolf.quizzy.models.Player;

public interface ScoringStrategy {
    int calculateScore(GameSession session, Question question, Player player, boolean isCorrect, int responseTimeMs);
}