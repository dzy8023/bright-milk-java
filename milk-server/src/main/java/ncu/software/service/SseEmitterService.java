package ncu.software.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
public interface SseEmitterService {
    SseEmitter connect(Long userId);
    void sendMessage(Long userId, Object message);
    void sendToAll(Object message);
    void disconnect(Long userId);
    int heartBeat();
}
