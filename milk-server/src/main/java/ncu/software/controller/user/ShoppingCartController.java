package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.ShoppingCartDTO;
import ncu.software.entity.ShoppingCart;
import ncu.software.result.Result;
import ncu.software.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */

    @PostMapping("/add")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车:{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 获取购物车列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>>list(){
        return Result.success(shoppingCartService.getShoppingCart());
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中一个商品:{}",shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
    @DeleteMapping
    public Result<String>del(Long id){
        shoppingCartService.delShoppingCart(id);
        return Result.success();
    }
}
