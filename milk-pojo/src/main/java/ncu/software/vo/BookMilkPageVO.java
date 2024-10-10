package ncu.software.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookMilkPageVO implements Serializable {
    private Long id;
    private Long userId;
    //用户名
    private String name;
    private Long orderId;
    //订单号
    private String number;
    //类型 0到店自取，1送货上门
    private Integer type;
    // 订奶状态 1待付款 2待完成  3已完成 4已取消 5退款
    private Integer status;
    private LocalDate startTime;
    private LocalDate endTime;
}
