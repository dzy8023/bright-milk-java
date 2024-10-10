package ncu.software.controller.admin;

import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.BookMilkService;
import ncu.software.vo.BookMilkVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/bookMilk")
@Slf4j
public class BookMikController {

    @Autowired
    private BookMilkService bookMilkService;

    /**
     * 分页查询订单
     * @param bookMilkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BookMilkPageQueryDTO bookMilkPageQueryDTO){
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

}
