package ncu.software.task;

import lombok.extern.slf4j.Slf4j;
import ncu.software.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SseTask {
    @Autowired
    private SseEmitterService sseEmitterService;
    @Scheduled(cron = "0/30 * * * * *")
    public void sendSse() {
        int count=sseEmitterService.heartBeat();
        log.info("Send sse to all-"+count);
    }
}
