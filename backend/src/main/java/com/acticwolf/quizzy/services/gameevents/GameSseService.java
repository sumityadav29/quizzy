package com.acticwolf.quizzy.services.gameevents;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface GameSseService {
    SseEmitter createEmitter(Integer sessionId, Integer playerId);
    void sendToSession(Integer sessionId, GameEventsRealTimeServiceImpl.GameEvent eventKey, Object data);
    void sendToPlayer(Integer sessionId, Integer playerId, GameEventsRealTimeServiceImpl.GameEvent eventKey, Object data);
}