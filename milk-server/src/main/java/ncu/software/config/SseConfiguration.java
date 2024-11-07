package ncu.software.config;

import lombok.extern.slf4j.Slf4j;
import ncu.software.handler.SseAdminHandler;
import ncu.software.handler.SseUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SseConfiguration {
    @Bean
    public SseUserHandler sseUserHandler() {
        log.info("创建 SseUserHandler bean...");
        return new SseUserHandler();
    }
    @Bean
    public SseAdminHandler sseAdminHandler() {
        log.info("创建 SseAdminHandler bean...");
        return new SseAdminHandler();
    }
}
