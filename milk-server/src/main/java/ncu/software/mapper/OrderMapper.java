package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.dto.OrderPageQueryDTO;
import ncu.software.entity.Orders;
import ncu.software.vo.MilkSaleDataVO;
import ncu.software.vo.OrderBasicDetailVO;
import ncu.software.vo.OrderOfOneDayVO;
import ncu.software.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 根据条件分页查询订单
     * @param orderPageQueryDTO
     * @return
     */
    Page<OrderVO> pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 更新订单状态
     * @param orders
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void updateStatus(Orders orders);

    /**
     * 插入订单
     * @param orders
     */
    void insert(Orders orders);

    @Update("update orders set actual_payment = #{actualPayment} where id = #{id}")
    void updateActualPayment(Orders orders);
    @Delete("delete from orders where id = #{id}")
    void deleteById(Long id);
    List<HashMap<String, Double>> getTurnoverStatistics(LocalDateTime beginDateTime, LocalDateTime endDateTime);
    List<HashMap<String, Integer>> getOrderStatistics(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    List<OrderBasicDetailVO> getSaleStatistics(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    List<HashMap<String, Integer>> getMilkSaleData(LocalDateTime beginDateTime, LocalDateTime endDateTime, Long id);

    List<MilkSaleDataVO> getMilksSaleData(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    List<OrderOfOneDayVO> getOrderOfOneDayStatistics(LocalDateTime beginDateTime, LocalDateTime endDateTime);
@Update("update orders set status = #{status}, actual_payment = #{actualPayment},update_time = #{updateTime} where id = #{id}")
    void payment(Orders orders);
}
