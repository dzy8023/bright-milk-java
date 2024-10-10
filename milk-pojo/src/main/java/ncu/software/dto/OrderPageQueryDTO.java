package ncu.software.dto;

import lombok.Data;

@Data
public class OrderPageQueryDTO {
    // 页码
    private int page;
    // 每页数量
    private int pageSize;
    private Long userId;
    //状态 0表示禁用 1表示启用
    private Integer status;
}
