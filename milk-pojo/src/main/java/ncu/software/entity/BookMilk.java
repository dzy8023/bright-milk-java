package ncu.software.entity;

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
public class BookMilk implements Serializable {

    /**
     * 订单状态 1待付款 2待完成  3已完成 4已取消 5退款
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_COMPLETED = 2;
    public static final Integer COMPLETED = 3;
    public static final Integer CANCELLED = 4;
    public static final Integer REFUND = 5;
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long orderId;
    //配送方式 0:到店自取 1:送货上门
    private Integer type;
    private Integer status;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate startTime;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate endTime;
    //当订单状态发生改变时的修改人
    private Long updateUser;
}
