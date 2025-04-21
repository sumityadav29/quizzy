package com.acticwolf.quizzy.services;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseServiceImpl implements SseService {

    private final Map<Integer, Map<Integer, SseEmitter>> sessionEmitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createEmitter(Integer sessionId, Integer playerId) {
        SseEmitter emitter = new SseEmitter(0L);

        sessionEmitters
                .computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                .put(playerId, emitter);

        emitter.onCompletion(() -> sessionEmitters.get(sessionId).remove(playerId));
        emitter.onTimeout(() -> sessionEmitters.get(sessionId).remove(playerId));

        return emitter;
    }

    @Override
    public void sendToSession(Integer sessionId, String eventKey, Object data) {
        Map<Integer, SseEmitter> emitters = sessionEmitters.getOrDefault(sessionId, Map.of());

        for (SseEmitter emitter : emitters.values()) {
            try {
                emitter.send(SseEmitter.event().name(eventKey).data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    @Override
    public void sendToPlayer(Integer sessionId, Integer playerId, String eventKey, Object data) {
        Map<Integer, SseEmitter> emitters = sessionEmitters.getOrDefault(sessionId, Map.of());
        SseEmitter emitter = emitters.get(playerId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventKey).data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}