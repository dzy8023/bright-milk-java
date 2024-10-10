package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class MilkPageQueryDTO implements Serializable {
    // 页码
    private int page;
    // 每页数量
    private int pageSize;
    private String name;
    //分类id
    private Integer categoryId;
    //状态 0表示禁用 1表示启用
    private Integer status;
    private Integer packId;
}
