package ncu.software.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String phone;
    @JsonIgnore
    private String password;
    //余额
    private BigDecimal balance;
    private Integer status;
    private LocalDateTime createTime;

}
