package com.acticwolf.quizzy.repositories;

import com.acticwolf.quizzy.models.GameSessionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionAnswerRepository extends JpaRepository<GameSessionAnswer, Integer> {
    Optional<GameSessionAnswer> findByPlayerIdAndQuestionId(Integer playerId, Integer questionId);

    List<GameSessionAnswer> findByPlayerId(Integer playerId);

    List<GameSessionAnswer> findByQuestionId(Integer questionId);
}