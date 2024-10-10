package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.annotation.AutoFill;
import ncu.software.dto.EmployeePageQueryDTO;
import ncu.software.entity.Employee;
import ncu.software.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);

    /**
     * 插入员工数据
     *
     * @param employee
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "values" +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据主键动态修改属性
     *
     * @param employee
     */
    @AutoFill(value= OperationType.UPDATE)
    void updateInfo(Employee employee);

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);

    /**
     * 更改密码
     *
     * @param employee
     */
    @AutoFill(value= OperationType.UPDATE)
    @Update("update employee set password=#{password} where id=#{id}")
    void updatePassword(Employee employee);

    /**
     * 删除员工
     * @param id
     */
    @Delete("delete from employee where id=#{id}")
    void deleteById(Long id);
}
