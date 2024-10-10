package ncu.software.mapper;

import ncu.software.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据用户id和商品id查询购物车
     *
     * @param userId
     * @param milkId
     * @return
     */
    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND milk_id = #{milkId}")
    ShoppingCart getByUIDMID(Long userId, Long milkId);
@Insert("INSERT INTO shopping_cart(user_id, milk_id,name,image,number,amount,create_time) " +
        " VALUES " +
        "(#{userId}, #{milkId}, #{name}, #{image},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);
@Update("UPDATE shopping_cart SET number = #{number}, amount = #{amount} WHERE id=#{id}")
    void updateNA(ShoppingCart shoppingCart);
@Select("SELECT * FROM shopping_cart WHERE user_id = #{id}")
    List<ShoppingCart> list(Long id);
@Delete("DELETE FROM shopping_cart WHERE user_id = #{id}")
    void deleteByUID(Long id);
@Delete("DELETE FROM shopping_cart WHERE id = #{id}")
    void deleteByID(Long id);
}
