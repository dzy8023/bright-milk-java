package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.entity.BookMilk;
import ncu.software.vo.BookMilkPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMilkMapper {
    /**
     * 插入一条记录,并且要使用到主键
     * @param bookMilk
     */
    void insert(BookMilk bookMilk);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Select("select * from book_milk where id = #{id}")
    BookMilk getById(Long id);

    /**
     * 分页查询
     * @param bookMilkPageQueryDTO
     * @return
     */
    Page<BookMilkPageVO> pageQuery(BookMilkPageQueryDTO bookMilkPageQueryDTO);

    /**
     * 获取主键列表
     * @param bookMilk
     * @return
     */
    List<Long> getIds(BookMilk bookMilk);

    /**
     * 根据状态获取主键列表
     * @param status
     * @return
     */
    @Select("select id from book_milk where status = #{status}")
    List<Long> getIdsByStatus(Integer status);
@Select("select * from book_milk where status = #{status}")
    List<BookMilk> getByStatus(Integer status);
}
