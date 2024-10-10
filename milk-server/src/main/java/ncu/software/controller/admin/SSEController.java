package ncu.software.controller.admin;

import lombok.extern.slf4j.Slf4j;
import ncu.software.context.BaseContext;
import ncu.software.result.Result;
import ncu.software.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/admin/sse")
@CrossOrigin
public class SSEController {
    @Autowired
    private SseEmitterService sseEmitterService;
    @GetMapping(value = "/subscribe",produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter  subscribe() {
        return sseEmitterService.connect(BaseContext.getCurrentId());
    }
    @PostMapping(value = "/unsubscribe")
    public Result<String> publish() {
        sseEmitterService.disconnect(BaseContext.getCurrentId());
        return Result.success();
    }
}
