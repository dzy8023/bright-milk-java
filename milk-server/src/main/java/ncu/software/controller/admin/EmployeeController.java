package ncu.software.controller.admin;

import ncu.software.constant.JwtClaimsConstant;
import ncu.software.dto.EmployeeDTO;
import ncu.software.dto.EmployeeLoginDTO;
import ncu.software.dto.EmployeePageQueryDTO;
import ncu.software.entity.Employee;
import ncu.software.properties.JwtProperties;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.EmployeeService;
import ncu.software.utils.JwtUtil;
import ncu.software.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        employeeService.logout();
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用/禁用员工账号
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(Long id, @PathVariable Integer status) {
        log.info("启用/禁用员工账号：{},{}", id, status);
        employeeService.startOrStop(id, status);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }


    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息：{}", employeeDTO);
        employeeService.updateInfo(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/editPassword")
    public Result<String> updatePassword(String oldPassword, String newPassword) {
        log.info("修改密码：{}", oldPassword + "," + newPassword);
        employeeService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 删除员工信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable Long id) {
        log.info("删除员工信息：{}", id);
        employeeService.deleteById(id);
        return Result.success();
    }

}
