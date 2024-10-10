package ncu.software.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //名称
    private String name;
    //图片
    private String image;
    //订单id
    private Long orderId;
    //牛奶id
    private Long milkId;
    //金额
    private BigDecimal amount;
    //数量
    private Integer number;
}
