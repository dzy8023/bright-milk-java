package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.config.sse.SseEvent;
import ncu.software.constant.JwtClaimsConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.UserDTO;
import ncu.software.dto.UserLoginDTO;
import ncu.software.dto.UserRegisterDTO;
import ncu.software.entity.SseMessage;
import ncu.software.entity.User;
import ncu.software.enumeration.MessageType;
import ncu.software.properties.JwtProperties;
import ncu.software.result.Result;
import ncu.software.service.SseEmitterService;
import ncu.software.service.UserService;
import ncu.software.utils.JwtUtil;
import ncu.software.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController("userUserController")
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private SseEmitterService sseEmitterService;

    @GetMapping("/test")
    public Result<String> test(@RequestParam(value = "userId", required = false) String userId, String message) {
        log.info("test: userId={}, message={}", userId, message);
        //构造json数据
        SseMessage sseMessage = new SseMessage();
        sseMessage.setMessage(message);
        sseEmitterService.sendMessage(sseMessage,"user-"+BaseContext.getCurrentId(),"admin-"+userId,MessageType.ALL);
        return Result.success("发送成功");
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .balance(user.getBalance())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 查询当前用户详细信息
     *
     * @return
     */
    @GetMapping("/info")
    public Result<User> info() {
        return Result.success(userService.info());
    }

    /**
     * 修改当前用户信息
     *
     * @param userDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody UserDTO userDTO) {
        log.info("用户修改信息：{}", userDTO);
        userService.update(userDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/editPassword")
    public Result<String> updatePassword(String oldPassword, String newPassword) {
        log.info("用户修改密码：{}", oldPassword + "," + newPassword);
        userService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 注销
     *
     * @return
     */
    @DeleteMapping
    public Result<String> destroy() {
        log.info("用户注销");
        userService.destroy();
        return Result.success();
    }


}
