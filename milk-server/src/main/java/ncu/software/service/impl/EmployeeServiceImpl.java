package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ncu.software.constant.MessageConstant;
import ncu.software.constant.PasswordConstant;
import ncu.software.constant.StatusConstant;
import ncu.software.dto.EmployeeDTO;
import ncu.software.dto.EmployeeLoginDTO;
import ncu.software.dto.EmployeePageQueryDTO;
import ncu.software.entity.Employee;
import ncu.software.exception.AccountLockedException;
import ncu.software.exception.PasswordErrorException;
import ncu.software.mapper.EmployeeMapper;
import ncu.software.result.PageResult;
import ncu.software.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ncu.software.exception.AccountNotFoundException;
import org.springframework.util.DigestUtils;
import ncu.software.context.BaseContext;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //1.根据用户名查询员工
        Employee employee = employeeMapper.selectByUsername(username);
        //2.处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码对比(先md5加密，在比较)
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("加密后的密码：" + password);
        if (!password.equals(employee.getPassword())) {
            //账号或密码错误
            throw new PasswordErrorException(MessageConstant.ACCOUNT_OR_PASS_ERROR);
        }
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置帐号状态，默认1正常，0锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
       //pagehelper动态追加limit条件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用员工
     *
     * @param id
     * @param status
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        //构造实体对象，增加方法的通用性，使用构造器
        Employee employee = Employee.builder().status(status).id(id).updateTime(LocalDateTime.now()).updateUser(BaseContext.getCurrentId()).build();
        employeeMapper.updateInfo(employee);
    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 更新员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void updateInfo(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        if(employeeDTO.getId()==null){
            throw new AccountNotFoundException(MessageConstant.ID_IS_NULL);
        }
        BeanUtils.copyProperties(employeeDTO, employee);
        //第一个参数是Employee，mapper自动填充
        employeeMapper.updateInfo(employee);
    }

    /**
     * 更新员工密码
     *
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
        Employee employee = employeeMapper.getById(id);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        // 进行md5加密，然后再进行比对
        String password = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //3、修改密码
        employee.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        employeeMapper.updatePassword(employee);
        BaseContext.removeCurrentId();
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        BaseContext.removeCurrentId();
    }

    /**
     * 根据id删除员工
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        employeeMapper.deleteById(id);
    }
}
