package ncu.software.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBasicDetailVO implements Serializable {
    private long milkId;
    private String name;
    private String image;
    private int number;
}
