package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.annotation.AutoFill;
import ncu.software.dto.CategoryPageQueryDTO;
import ncu.software.entity.Category;
import ncu.software.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    /**
     * 添加分类
     *
     * @param category
     */
    @Insert("insert into category(type, name, status, create_time, update_time, create_user, update_user) " +
            " VALUES "+
    "(#{type}, #{name}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    /**
     * 修改分类
     *
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);
}
