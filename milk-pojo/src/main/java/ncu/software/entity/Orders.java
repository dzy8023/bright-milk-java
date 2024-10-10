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
public class Orders implements Serializable {
    /**
     * 订单状态 1待付款 2待完成  3已完成 4已取消 5退款
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_COMPLETED = 2;
    public static final Integer COMPLETED = 3;
    public static final Integer CANCELLED = 4;
    public static final Integer REFUND = 5;
    private Long id;
    //订单号
    private String number;
    //订单状态 1待付款 2待完成 3已完成 4已取消 5退款
    private Integer status;
    //用户id
    private Long userId;
    //实收金额
    private BigDecimal actualPayment;
    //应收金额
    private BigDecimal duePayment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long updateUser;
}
