package ncu.software.service;

import ncu.software.dto.*;
import ncu.software.entity.User;
import ncu.software.result.PageResult;

public interface UserService {
    /**
     * 用户注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取用户信息
     * @return
     */
    User info();

    /**
     * 更新用户信息
     * @param userDTO
     */
    void update(UserDTO userDTO);

    /**
     * 更新用户密码
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 注销用户
     */
    void destroy();

    /**
     * 重置用户密码
     * @param id
     */
    void resetPassword(Long id);

    /**
     * 用户充值
     * @param userChargeDTO
     */
    void charge(UserChargeDTO userChargeDTO);

    void deleteById(Long id);

    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    void startOrStop(Long id, Integer status);
}
