package ncu.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //订奶id
    private Long bookId;
    //详细地址信息
    private String detail;
}
