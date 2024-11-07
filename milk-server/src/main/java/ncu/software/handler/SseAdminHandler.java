package ncu.software.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SseAdminHandler extends SseHandler {
    @Override
    public void sendToAll(Object message) {
        super.sendToAll(message);
    }

}
