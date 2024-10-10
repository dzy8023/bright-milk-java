package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMilkVO implements Serializable {
    //牛奶id
    private Long id;
    private String name;
    private String categoryName;
    private BigDecimal price;
    private String packName;
    private String image;
}
