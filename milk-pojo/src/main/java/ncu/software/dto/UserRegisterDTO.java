package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {
    private Long id;
    private String name;
    private String password;
    private String phone;
}
