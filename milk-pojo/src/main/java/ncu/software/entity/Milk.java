package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Milk implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    //牛奶名称
    private String name;
    //分类id
    private Long categoryId;
    //0 停售 1 起售
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
