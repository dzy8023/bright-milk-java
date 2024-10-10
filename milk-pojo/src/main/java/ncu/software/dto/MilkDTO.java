package ncu.software.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MilkDTO implements Serializable {
    // 牛奶id
    private Long id;
    private String name;
    // 牛奶类型
    private Long categoryId;
    // 牛奶状态
    private Integer status;
    // 牛奶价格
    private BigDecimal price;
    // 牛奶图片
    private String image;
    // 牛奶规格
    private Integer standard;
    // 牛奶包装id
    private Integer packId;
    // 牛奶描述
    private String description;
    // 牛奶库存
    private Integer amount;
}
