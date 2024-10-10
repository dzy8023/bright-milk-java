package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long milkId;
    //牛奶名称
    private String name;
    //牛奶图片
    private String  image;
    //数量
    private Integer number;
    //金额
    private BigDecimal amount;
    private LocalDateTime createTime;
}
