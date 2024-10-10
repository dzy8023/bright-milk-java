package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pack implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
}
