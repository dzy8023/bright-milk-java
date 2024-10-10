package ncu.software.mapper;

import ncu.software.entity.MilkDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
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
}
