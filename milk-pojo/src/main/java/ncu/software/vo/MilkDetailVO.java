package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilkDetailVO implements Serializable {
    //牛奶id
    private Long id;
    private String name;
    private Long categoryId;
    //类型
    private Integer type;
    private Integer status;
    private BigDecimal price;
    private String image;
    private Integer standard;
    private String packId;
    private String description;
    private Integer amount;
}
