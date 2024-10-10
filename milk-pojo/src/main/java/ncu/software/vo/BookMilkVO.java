package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncu.software.entity.BookMilkOrder;
import ncu.software.entity.OrderDetail;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMilkVO implements Serializable {
    private Long id;
    //订单号
    private String number;
    //类型 0到店自取，1送货上门
    private Integer type;
    // 订奶状态 1待付款 2待完成  3已完成 4已取消 5退款
    private Integer status;
    private LocalDate startTime;
    private LocalDate endTime;
    List<BookMilkOrder> bookMilkOrderList;
    List<OrderDetail> milkDetailList;
}
