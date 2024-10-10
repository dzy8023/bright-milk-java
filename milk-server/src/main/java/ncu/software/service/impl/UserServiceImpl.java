package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ncu.software.constant.AmountConstant;
import ncu.software.constant.MessageConstant;
import ncu.software.constant.PasswordConstant;
import ncu.software.constant.StatusConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.*;
import ncu.software.entity.Employee;
import ncu.software.entity.User;
import ncu.software.exception.*;
import ncu.software.mapper.UserMapper;
import ncu.software.result.PageResult;
import ncu.software.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import ncu.software.exception.AccountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 注册
     * @param userRegisterDTO
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User user=new User();
        if(userRegisterDTO.getPhone()==null||userRegisterDTO.getName()==null){
            throw new RegisterFailedException(MessageConstant.REGISTER_FAILED);
        }
        //判断账号是否已存在
        if(userMapper.getByPhone(userRegisterDTO.getPhone())!=null){
            throw new RegisterFailedException(MessageConstant.PHONE_ALREADY_EXISTS);
        }
        BeanUtils.copyProperties(userRegisterDTO,user);
        //密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()));
        user.setBalance(AmountConstant.DEFAULT_BIGDECIMAL);
        user.setStatus(StatusConstant.DISABLE);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user;
        user=getUser(userLoginDTO.getPhone());
        String password=userLoginDTO.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(user.getPassword())){
            //账号或密码错误
            throw new PasswordErrorException(MessageConstant.ACCOUNT_OR_PASS_ERROR);
        }
        if(Objects.equals(user.getStatus(), StatusConstant.DISABLE)){
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return user;
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        //因该要在redis中删去当前用户的token
        BaseContext.removeCurrentId();
    }

    /**
     * 获取当前用户信息
     * @return
     */
    @Override
    public User info() {
        return userMapper.getById(BaseContext.getCurrentId());
    }

    /**
     * 更新用户信息
     * @param userDTO
     */
    @Override
    public void update(UserDTO userDTO) {
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setId(BaseContext.getCurrentId());
        userMapper.update(user);
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        if (!oldPassword.matches("[0-9|a-z]{6,20}") || !newPassword.matches("[0-9|a-z]{6,20}")) {
            throw new PasswordErrorException(MessageConstant.WRONG_PASSWORD);
        }
        Long id = BaseContext.getCurrentId();
        //1、根据用户id查询数据库中的数据
        User user = userMapper.getById(id);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        // 进行md5加密，然后再进行比对
        String password = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //3、修改密码
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userMapper.updatePassword(user);
        //
        BaseContext.removeCurrentId();
    }

    /**
     * 注销账号
     */
    @Override
    public void destroy() {
        Long id = BaseContext.getCurrentId();
        userMapper.deleteById(id);
    }
    private User getUser(String phone) {
        User user;
        if(phone!=null) {
            user=userMapper.getByPhone(phone);
        }else {
                throw new LoginFailedException(MessageConstant.PHONE_IS_NULL);
            }
        if(user==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return user;
    }

    /**
     * 重置密码
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        User user;
        user=userMapper.getById(id);
        if(user==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        userMapper.updatePassword(user);
    }

    /**
     * 充值
     * @param userChargeDTO
     */
    @Override
    public void charge(UserChargeDTO userChargeDTO) {
        User user;
        user=getUser(userChargeDTO.getPhone());
        user.setBalance(BigDecimal.valueOf(userChargeDTO.getCharge()).add(user.getBalance()));
        if(user.getBalance().compareTo(AmountConstant.DEFAULT_BIGDECIMAL)<=0){
            throw new ChargeFailedException(MessageConstant.CHARGE_FAILED);
        }
        userMapper.updateBalanceById(user);
    }

    @Override
    public void deleteById(Long id) {
        User user=userMapper.getById(id);
        if(user==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(Objects.equals(user.getStatus(), StatusConstant.ENABLE)){
            throw new AccountException(MessageConstant.ACCOUNT_STATUS_ERROR);
        }
        userMapper.deleteById(id);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<User> page=userMapper.pageQuery(userPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void startOrStop(Long id, Integer status) {
            //构造实体对象，增加方法的通用性，使用构造器
            User user = User.builder().status(status).id(id).build();
            userMapper.update(user);
    }
}









