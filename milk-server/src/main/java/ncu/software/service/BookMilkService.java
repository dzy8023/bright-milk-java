package ncu.software.service;

import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.vo.BookMilkOrderVO;
import ncu.software.vo.BookMilkVO;
import ncu.software.vo.OrderVO;

import java.time.LocalDate;

public interface BookMilkService {
    /**
     * 分页查询
     * @param bookMilkPageQueryDTO
     * @return
     */
    PageResult pageQuery(BookMilkPageQueryDTO bookMilkPageQueryDTO);

    /**
     * 创建订单
     * @return
     */
    BookMilkOrderVO createBookMilk(LocalDate startTime, LocalDate endTime,Integer type);

    /**
     * 支付订单
     * @param id
     */
    void bookMilkPayment(Long id);

    /**
     * 获取订奶详情
     * @param id
     * @return
     */
    BookMilkVO getBookMilk(Long id);
}
