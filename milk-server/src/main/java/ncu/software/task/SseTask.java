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
    // 每分钟发送一次 心跳包
    @Scheduled(cron = "0 * * * * *")
    public void sendSse() {
        String count=sseEmitterService.heartBeat();
        log.info("Send sse to all-"+"admin["+count.split("-")[0]+"] user["+count.split("-")[1]+"]");
    }
}