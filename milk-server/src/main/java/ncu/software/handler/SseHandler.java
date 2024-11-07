package ncu.software.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SseHandler {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    public SseEmitter connect(Long userId) {
        //设置超时时间为0，表示不超时
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> {
            log.info("结束连接-{}", userId);
        });
        emitter.onError((e) -> {
            log.error("连接出错-{}:{}", userId, e.getMessage());
            disconnect(userId);
        });
        emitter.onTimeout(() -> {
            log.info("连接超时-{}", userId);
            disconnect(userId);
        });
        try {
            emitter.send(SseEmitter.event().name("init").data("连接已建立"));
        } catch (IOException e) {
            log.error("发送初始消息失败-{}:{}", userId, e.getMessage());
        }
        if(!sseEmitterMap.containsKey(userId)){
            counter.incrementAndGet();
        }
        sseEmitterMap.put(userId, emitter);
        log.info("创建新的sse连接，当前员工：{}", userId);
        return emitter;
    }
    public void sendMessage(Long userId, Object message) {
        if (sseEmitterMap.containsKey(userId)) {
            try {
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                log.error("推送异常-{}:{}", userId, e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            log.warn("SSE 发送信息异常，用户不存在：id = {} ", userId);
        }
    }
    public void sendToAll(Object message) {
        for (Map.Entry<Long, SseEmitter> entry : sseEmitterMap.entrySet()) {
            try {
                if (message != null) {
                    entry.getValue().send(message);
                }
            } catch (IOException e) {
                log.error("推送异常-{}:{}", entry.getKey(), e.getMessage());
            }
        }
    }
    public void disconnect(Long userId) {
        if (sseEmitterMap.containsKey(userId)) {
            counter.decrementAndGet();
            sseEmitterMap.remove(userId);
        }
       log.info("sse连接断开，当前员工：{}", userId);
    }
    public int heartBeat() {
        for (Map.Entry<Long, SseEmitter> entry : sseEmitterMap.entrySet()) {
            try {
                entry.getValue().send(SseEmitter.event().name("heartbeat").data("heartbeat"));
            } catch (IOException e) {
                log.error("心跳异常-{}:{}", entry.getKey(), e.getMessage());
            }
        }
        return counter.get();
    }
}
