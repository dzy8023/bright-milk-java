package ncu.software.service;

import ncu.software.dto.EmployeeDTO;
import ncu.software.dto.EmployeeLoginDTO;
import ncu.software.dto.EmployeePageQueryDTO;
import ncu.software.entity.Employee;
import ncu.software.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工账号
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    void updateInfo(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 删除员工信息
     * @param id
     */
    void deleteById(Long id);
}
