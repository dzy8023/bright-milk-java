package ncu.software.service;

import ncu.software.dto.ShoppingCartDTO;
import ncu.software.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 向购物车添加商品
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 获取购物车列表
     * @return
     */
    List<ShoppingCart> getShoppingCart();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

    /**
     * 从购物车移除一个商品
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 从购物车移除一个商品
     * @param id
     */
    void delShoppingCart(Long id);
}
