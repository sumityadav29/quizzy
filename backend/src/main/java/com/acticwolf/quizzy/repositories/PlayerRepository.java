package com.acticwolf.quizzy.repositories;

import com.acticwolf.quizzy.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findBySessionId(Integer sessionId);
    Optional<Player> findByPlayerToken(String playerToken);
}