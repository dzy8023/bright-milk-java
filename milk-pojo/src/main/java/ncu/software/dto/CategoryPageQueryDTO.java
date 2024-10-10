package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CategoryPageQueryDTO implements Serializable {
    //页码
    private int page;
    //每页记录数
    private int pageSize;
    //分类名称
    private String name;
    //分类类型 1低温奶  2常温奶
    private Integer type;
    private Integer status;
}
