package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.context.BaseContext;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.BookMilkService;
import ncu.software.vo.BookMilkOrderVO;
import ncu.software.vo.BookMilkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController("userBookMilkController")
@RequestMapping("/user/bookMilk")
@Slf4j
public class BookMilkController {
    @Autowired
    private BookMilkService bookMilkService;

    /**
     * 分页查询订单
     * @param bookMilkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BookMilkPageQueryDTO bookMilkPageQueryDTO){
        //用户只能查到自己的订单数据
        bookMilkPageQueryDTO.setUserId(BaseContext.getCurrentId());
        return Result.success(bookMilkService.pageQuery(bookMilkPageQueryDTO));
    }

    /**
     * 根据id查询订奶详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<BookMilkVO> getBookMilk(@PathVariable Long id){
        return Result.success(bookMilkService.getBookMilk(id));
    }

    /**
     * 创建订单
     * @return
     */
    @PostMapping("/create")
    public Result<BookMilkOrderVO>createBookMilk( @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime ,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime
    ,Integer type) {
        return Result.success(bookMilkService.createBookMilk(startTime, endTime,type));
    }

    /**
     * 支付订单
     * @param id
     * @return
     */
    @GetMapping("/pay")
    public Result<String>bookMilkPayment(Long id){
        bookMilkService.bookMilkPayment(id);
        return Result.success();
    }
}
