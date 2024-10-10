package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.dto.UserPageQueryDTO;
import ncu.software.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 用户注册
     *
     * @param user
     */
    @Insert("INSERT INTO user(name,phone,password,status,create_time) " +
            " VALUES " +
            "(#{name},#{phone},#{password},#{status},#{createTime})")
    void insert(User user);

    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User getByPhone(String phone);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(Long id);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void update(User user);

    /**
     * 更新用户密码
     *
     * @param user
     */
    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    void updatePassword(User user);

    /**
     * 删除用户
     *
     * @param id
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 更新用户余额
     *
     * @param user
     */
    @Update("UPDATE user SET balance = #{balance} WHERE id = #{id}")
    void updateBalanceById(User user);

    /**
     * 分页查询
     *
     * @param userPageQueryDTO
     * @return
     */
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);


    List<HashMap<String, Integer>> getNewUserStatistics(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    @Select("SELECT COUNT(*) FROM user WHERE create_time < #{endDateTime}")
    Integer countUserBefore(LocalDateTime endDateTime);
}
