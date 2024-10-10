package ncu.software.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginDTO implements Serializable {
    //登录时前端传来的数据，phone和email二选一
    private String phone;
    private String password;
}
