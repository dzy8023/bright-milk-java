package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncu.software.entity.OrderDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    private Long id;
    private String number;
    private Integer status;
    private Long userId;
    private String name;
    private String phone;
    private BigDecimal actualPayment;
    private BigDecimal duePayment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<OrderDetail> orderDetailList;
}
