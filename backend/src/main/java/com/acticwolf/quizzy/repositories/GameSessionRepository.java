package com.acticwolf.quizzy.repositories;

import com.acticwolf.quizzy.models.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
    Optional<GameSession> findByRoomCode(String roomCode);
}