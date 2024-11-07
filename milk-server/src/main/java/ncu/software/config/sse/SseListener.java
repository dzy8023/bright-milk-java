package ncu.software.config.sse;

import lombok.extern.slf4j.Slf4j;
import ncu.software.enumeration.MessageType;
import ncu.software.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SseListener implements ApplicationListener<SseEvent> {
    @Autowired
    private SseEmitterService sseEmitterService;
    @Override
    public void onApplicationEvent(SseEvent event) {
        if(event.getType()== MessageType.ALL){
           sseEmitterService.sendToAll(event.getMsg());
        } else if (event.getType() == MessageType.SIGNAL) {
            sseEmitterService.sendMessage(event.getReceiverId(),event.getMsg());
        }
    }
}
