package ncu.software.mapper;

import ncu.software.dto.MilkDTO;
import ncu.software.entity.MilkDetail;
import ncu.software.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mapper
public interface MilkDetailMapper {
    /**
     * 向牛奶详情表添加数据
     * @param milkDetail
     */
    @Insert("insert into milk_detail( milk_id, price, image, standard, pack_id, description,amount) " +
            " VALUES "+
            "( #{milkId},#{price}, #{image}, #{standard}, #{packId}, #{description}, #{amount})")
    void insert(MilkDetail milkDetail);

    /**
     * 根据牛奶id更新牛奶详情
     * @param milkDetail
     */
    void updateByMID(MilkDetail milkDetail);

    /**
     * 根据牛奶id删除牛奶详情
     * @param id
     */
    @Delete("delete from milk_detail where milk_id = #{id}")
    void deleteByMId(Long id);

    /**
     * 批量库存更新
     * @param milkDetailList
     */

    void updateBatch(List<MilkDetail> milkDetailList);

    /**
     * 根据牛奶id添加库存
     * @param id
     * @param amount
     * @param updateTime
     * @param updateUser
     */
    @Update("update milk_detail md left join milk m on md.milk_id=m.id set md.amount = md.amount + #{amount} " +
            ",m.update_time=#{updateTime},m.update_user=#{updateUser} where md.milk_id = #{id}")
    void addAmountByMID(Long id,Integer amount,LocalDateTime updateTime,Long updateUser);

    /**
     * 根据牛奶id减少库存
     * @param milkDetail
     */
    @Update("update milk_detail md left join milk m on md.milk_id=m.id set md.amount =#{amount} where md.milk_id=#{milkId} ")
    void updateAmountByMID(MilkDetail milkDetail);
@Select("select milk_id,amount from milk_detail where milk_id in (#{milkIds})")
    List<MilkDTO> getMilkDTOListByIds(List<Long> milkIds);
@Select("select md.milk_id as milkId,od.name as name,md.image as image,od.number as number,od.amount as amount " +
        "from milk_detail md join milk m on md.id=m.id and m.status='1' " +
        "join (select milk_id,name,number,amount from order_detail where order_id = #{orderId}) od " +
        "on md.milk_id = od.milk_id and md.amount >= od.number")
         List<ShoppingCart> getOneMoreMilkDTOList(Long orderId);
}
