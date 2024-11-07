package ncu.software.service;

import ncu.software.entity.SseMessage;
import ncu.software.enumeration.MessageType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
public interface SseEmitterService{
    SseEmitter connect(String userId);
    void sendMessage(String userId, Object message);
    void sendToAll(Object message);
    void disconnect(String userId);
    String heartBeat();
   <T extends SseMessage> void sendMessage(T sseMessage, String publisherId, String receiverId, MessageType messageType);
    // 重载泛型方法，使用默认的 MessageType.Single
    default <T extends SseMessage> void sendMessage(T sseMessage, String publisherId, String receiverId) {
        sendMessage(sseMessage, publisherId, receiverId, MessageType.SIGNAL);
    }
}
