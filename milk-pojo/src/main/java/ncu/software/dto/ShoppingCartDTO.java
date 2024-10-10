package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class ShoppingCartDTO implements Serializable {
private Long milkId;
private Integer number;
}
