package ncu.software.service;

import ncu.software.dto.BookMilkOrderPageQueryDTO;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.result.PageResult;

public interface BookMilkOrderService {
    /**
     * 分页查询在book_milk.status为待完成对应的bmo
     * @param bookMilkOrderPageQueryDTO
     * @return
     */
    PageResult pageQuery(BookMilkOrderPageQueryDTO bookMilkOrderPageQueryDTO);

    /**
     * 更新状态
     * @param id
     */
    void complete(Long id);
}
