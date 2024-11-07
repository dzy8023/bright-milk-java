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
    private String receiverId;
    private String publisherId;
    private T msg;
    public SseEvent(Object source,String publisherId) {
        super(source);
        this.publisherId=publisherId;
    }
}
