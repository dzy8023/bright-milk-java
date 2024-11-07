package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.context.BaseContext;
import ncu.software.result.Result;
import ncu.software.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController("userSseController")
@RequestMapping("/user/sse")
@CrossOrigin
public class SSEController {
    @Autowired
    private SseEmitterService sseEmitterService;
    @GetMapping(value = "/subscribe",produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe() {
        return sseEmitterService.connect("user-"+ BaseContext.getCurrentId());
    }
    @PostMapping(value = "/unsubscribe")
    public Result<String> publish() {
        sseEmitterService.disconnect("user-"+BaseContext.getCurrentId());
        return Result.success();
    }
}