package ncu.software.controller.admin;

import lombok.extern.slf4j.Slf4j;
import ncu.software.context.BaseContext;
import ncu.software.dto.OrderPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
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
    public Result<PageResult> page(OrderPageQueryDTO orderPageQueryDTO){
        return Result.success(orderService.pageQuery(orderPageQueryDTO));
    }
}
