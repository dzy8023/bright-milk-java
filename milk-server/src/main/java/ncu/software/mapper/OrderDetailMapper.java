package ncu.software.mapper;

import ncu.software.dto.MilkDTO;
import ncu.software.entity.Milk;
import ncu.software.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 根据订单id查询订单详情
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
    List<OrderDetail> getOrderDetailList(Long id);

    /**
     * 向订单详情插入多条数据
     *
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    @Select("SELECT m.id,m.status as status ,md.amount as amount  FROM milk m ,milk_detail md " +
            "WHERE m.id IN (SELECT milk_id FROM order_detail WHERE order_id = #{id}) and m.id=md.milk_id")
    List<MilkDTO> getMilkDTOListByOID(Long id);

    @Delete("DELETE FROM order_detail WHERE order_id = #{id}")
    void deleteBatch(Long id);

    List<OrderDetail> getOrderDetailListByOrderIds(List<Long> ids);
}
