package ncu.software.controller.admin;

import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.BookMilkOrderPageQueryDTO;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.BookMilkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/bookMilkOrder")
@Slf4j
public class BookMilkOrderController {

    @Autowired
    private BookMilkOrderService bookMilkOrderService;

    /**
     * 分页查询订单
     * @param bookMilkOrderPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BookMilkOrderPageQueryDTO bookMilkOrderPageQueryDTO){
        return Result.success(bookMilkOrderService.pageQuery(bookMilkOrderPageQueryDTO));
    }

    /**
     * 根据bookMilkOrderId完成
     * @param id
     * @return
     */
    @GetMapping("/complete")
    public Result<String> complete(Long id){
        bookMilkOrderService.complete(id);
        return Result.success();
    }













}
