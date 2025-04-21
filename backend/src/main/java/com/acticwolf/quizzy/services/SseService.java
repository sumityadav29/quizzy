package com.acticwolf.quizzy.services;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter createEmitter(Integer sessionId, Integer playerId);
    void sendToSession(Integer sessionId, String eventKey, Object data);
    void sendToPlayer(Integer sessionId, Integer playerId, String eventKey, Object data);
}