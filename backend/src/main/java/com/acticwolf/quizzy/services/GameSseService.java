package com.acticwolf.quizzy.services;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface GameSseService {
    SseEmitter createEmitter(Integer sessionId, Integer playerId);
    void sendToSession(Integer sessionId, GameSessionRealTimeServiceImpl.GameEvent eventKey, Object data);
    void sendToPlayer(Integer sessionId, Integer playerId, GameSessionRealTimeServiceImpl.GameEvent eventKey, Object data);
}