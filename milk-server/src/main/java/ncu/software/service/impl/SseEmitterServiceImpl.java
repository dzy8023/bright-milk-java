package ncu.software.service.impl;

import lombok.extern.slf4j.Slf4j;
import ncu.software.config.sse.SseEvent;
import ncu.software.context.BaseContext;
import ncu.software.entity.SseMessage;
import ncu.software.enumeration.MessageType;
import ncu.software.handler.SseAdminHandler;
import ncu.software.handler.SseUserHandler;
import ncu.software.service.SseEmitterService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {
    @Autowired
    private SseUserHandler sseUserHandler;
    @Autowired
    private SseAdminHandler sseAdminHandler;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public SseEmitter connect(String userId) {
        Long id = Long.valueOf(userId.split("-")[1]);
        if(userId.split("-")[0].equals("admin")){
            return sseAdminHandler.connect(id);
        }else {
            return sseUserHandler.connect(id);
        }
    }
    @Override
    public void sendMessage(String userId, Object message) {
        Long id = Long.valueOf(userId.split("-")[1]);
        if(userId.split("-")[0].equals("admin")){
             sseAdminHandler.sendMessage(id, message);
        }else {
             sseUserHandler.sendMessage(id, message);
        }
    }
    @Override
    public void sendToAll(Object message) {
       sseAdminHandler.sendToAll(message);
    }
    @Override
    public void disconnect(String userId) {
        Long id = Long.valueOf(userId.split("-")[1]);
        if(userId.split("-")[0].equals("admin")){
            sseAdminHandler.disconnect(id);
        }else {
            sseUserHandler.disconnect(id);
        }
    }
    @Override
    public String heartBeat() {
        return sseAdminHandler.heartBeat()+"-"+sseUserHandler.heartBeat();
    }

    @Override
    public <T extends SseMessage> void sendMessage(T sseMessage,String publisherId, String receiverId, MessageType messageType) {
        SseEvent<T> sseEvent = new SseEvent<>(this,"user-"+ BaseContext.getCurrentId());
        sseMessage.setPublishTime(LocalDateTime.now());
        sseMessage.setPublisherId(BaseContext.getCurrentId());
        sseEvent.setMsg(sseMessage);
        sseEvent.setReceiverId(receiverId);
        sseEvent.setType(messageType);
        applicationEventPublisher.publishEvent(sseEvent);
    }
}
