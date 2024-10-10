package ncu.software.config.sse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ncu.software.enumeration.MessageType;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
@ToString
public class SseEvent<T>extends ApplicationEvent {

    private MessageType type=MessageType.SIGNAL;
    private Long userId;
    private T msg;
    public SseEvent(Object source) {
        super(source);
    }
}
