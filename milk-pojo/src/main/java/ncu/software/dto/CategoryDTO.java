package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CategoryDTO implements Serializable {
    //主键
    private Long id;
    //类型 1:低温奶 2:常温奶
    private Integer type;
    //分类名称
    private String name;
}
