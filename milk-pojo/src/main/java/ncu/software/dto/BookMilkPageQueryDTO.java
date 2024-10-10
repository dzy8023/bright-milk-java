package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookMilkPageQueryDTO implements Serializable {

    // 页码
    private int page;
    // 每页数量
    private int pageSize;
    private Long userId;
    //订单状态  1待付款 2待完成 3已完成 4已取消 5退款
    private Integer status;
}
