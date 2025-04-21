package com.acticwolf.quizzy.controllers;

import com.acticwolf.quizzy.models.GameSession;
import com.acticwolf.quizzy.models.Player;
import com.acticwolf.quizzy.repositories.GameSessionRepository;
import com.acticwolf.quizzy.repositories.PlayerRepository;
import com.acticwolf.quizzy.services.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class SseController {

    private final PlayerRepository playerRepository;
    private final GameSessionRepository gameSessionRepository;
    private final SseService sseService;

    @GetMapping("/sessions/code/{roomCode}/subscribe")
    public SseEmitter subscribeToEvents(
            @PathVariable String roomCode,
            @RequestParam String playerToken) {
        Player player = playerRepository.findByPlayerToken(playerToken)
                .orElseThrow(() -> new RuntimeException("Invalid player token"));

        GameSession session = gameSessionRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Invalid room code"));

        if (!player.getSession().getId().equals(session.getId())) {
            throw new RuntimeException("Player does not belong to this session");
        }

        return sseService.createEmitter(session.getId(), player.getId());
    }
}