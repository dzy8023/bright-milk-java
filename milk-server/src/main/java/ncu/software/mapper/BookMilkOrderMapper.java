package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.entity.BookMilkOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BookMilkOrderMapper {
    /**
     * 更新
     *
     * @param bookMilkOrder
     */
    void update(BookMilkOrder bookMilkOrder);

    /**
     * 插入
     * @param bookMilkOrder
     */
    @Insert("INSERT INTO book_milk_order(book_id,status,update_time) VALUES(#{bookId},#{status},#{updateTime})")
    void insert(BookMilkOrder bookMilkOrder);

    /**
     * 根据bookMilkId查询
     * @param id
     * @return
     */
    @Select("select * from book_milk_order where book_id=#{id}")
    List<BookMilkOrder> getBookMilkOrderList(Long id);

    /**
     * 根据bookMilkId查询某天的数据
     * @param bookMilkIds
     * @param day
     * @return
     */
    Page<BookMilkOrder> pageQuery(List<Long>bookMilkIds, LocalDate day);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from book_milk_order where id=#{id}")
    BookMilkOrder getById(Long id);

    /**
     * 根据ids批量更新状态
     * @param ids
     * @param status
     * @param updateTime
     */
    void updateStatusByIds(List<Long> ids, Integer status, LocalDateTime updateTime);

@Select("select id from book_milk_order where status=#{status}")
    List<Long> getIdsByStatus(Integer status);
@Select("select * from book_milk_order where book_id=#{bmId} and status=#{status} and date(update_time)=#{day}")
    List<BookMilkOrder> getByBIdDay(Long bmId, LocalDate day);
@Select("select * from book_milk_order where status=#{status} and date(update_time)<#{day}")
    List<BookMilkOrder> getIdsByStatusBefore(Integer status, LocalDate day);
}
