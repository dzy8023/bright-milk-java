package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long publisherId;
    private String message;
    private LocalDateTime publishTime;
}
