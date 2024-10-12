package ncu.software.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOfOneDayVO {
    private String date;
    private BigDecimal turnover;
    private Integer totalOrder;
    private Integer validOrder;
}
