package ncu.software.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.EmployeePageQueryDTO;
import ncu.software.dto.UserChargeDTO;
import ncu.software.dto.UserLoginDTO;
import ncu.software.dto.UserPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 重置用户密码
     *
     * @param id
     * @return
     */
    @PutMapping("/resetPassword/{id}")
    public Result<String> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success();
    }

    @PostMapping("/charge")
    public Result<String> charge(@RequestBody UserChargeDTO userChargeDTO) {
        userService.charge(userChargeDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @Operation(summary = "员工分页查询")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO) {
        log.info("员工分页查询：{}", userPageQueryDTO);

        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工信息")
    public Result<String>deleteById(@PathVariable Long id){
        log.info("删除员工信息：{}",id);
        userService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "启用/禁用员工账号")
    public Result<String> startOrStop(Long id, @PathVariable Integer status) {
        log.info("启用/禁用员工账号：{},{}", id, status);
        userService.startOrStop(id, status);
        return Result.success();
    }







}
