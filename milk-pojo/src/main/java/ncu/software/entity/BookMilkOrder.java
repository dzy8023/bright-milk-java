package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMilkOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    //取奶状态,1待完成,2已出库,3已完成,4补签,5取消
    public static final Integer TO_BE_COMPLETED = 1;
    public static final Integer OUT_OF_STOCK = 2;
    public static final Integer COMPLETED = 3;
    public static final Integer REPLENISH = 4;
    public static final Integer CANCELLED = 5;

    private Long id;
    private Long bookId;

    private Integer status;
    //取奶时间/配送时间
    private LocalDateTime updateTime;
    //修改人/配送人
    private Long updateUser;
}
