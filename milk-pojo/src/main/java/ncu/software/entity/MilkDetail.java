package ncu.software.entity;

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
public class MilkDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long milkId;
    //价格
    private BigDecimal price;
    private String image;
    //规格
    private Integer standard;
    //包装类型id
    private Integer packId;
    //描述信息
    private String description;
    //库存
    private Integer amount;
}
