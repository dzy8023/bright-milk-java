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
public class MilkSaleDataVO implements Serializable {
    //牛奶id
    private Long id;
    private String name;
    private String image;
    private Integer number;
    private BigDecimal totalAmount;
}
