package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.context.BaseContext;
import ncu.software.dto.OrderPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.OrderService;
import ncu.software.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单
     * @param orderPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(OrderPageQueryDTO orderPageQueryDTO) {
        //用户只能查到自己的订单数据
        orderPageQueryDTO.setUserId(BaseContext.getCurrentId());
        return Result.success(orderService.pageQuery(orderPageQueryDTO));
    }

    /**
     * 创建订单
     * @return
     */
    @PostMapping("/create")
    public Result<OrderVO> createOrder() {
        return Result.success(orderService.createOrder());
    }

    /**
     * 支付订单
     * @return
     */
    @PostMapping("/pay")
    public Result<String> orderPayment(Long id) {
        orderService.orderPayment(id);
        return Result.success();
    }

    @GetMapping()
    public Result<OrderVO> getOrderById(Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @DeleteMapping
    public Result<String> deleteOrderById(Long id) {
        orderService.deleteOrderById(id);
        return Result.success();
    }
    @PostMapping("/oneMore/{orderId}")
    public Result<String> oneMore(@PathVariable Long orderId) {
        orderService.oneMore(orderId);
        return Result.success();
    }

}





