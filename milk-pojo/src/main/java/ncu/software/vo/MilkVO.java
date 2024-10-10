package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilkVO implements Serializable {
    //牛奶id
    private Long id;
    private String name;
    private Long categoryId;
    private Integer status;
    private BigDecimal price;
    private String image;
    private Integer amount;
    private LocalDateTime updateTime;
}
