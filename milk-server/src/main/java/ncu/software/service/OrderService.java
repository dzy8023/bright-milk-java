package ncu.software.service;

import ncu.software.dto.OrderPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.vo.OrderVO;

public interface OrderService {
    /**
     * 分页查询订单
     * @param orderPageQueryDTO
     * @return
     */
    PageResult pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 创建订单
     * @return
     */
    OrderVO createOrder();

    /**
     * 支付订单
     * @param id
     */
    void orderPayment(Long id);

    OrderVO getOrderById(Long id);

    void deleteOrderById(Long id);

    void oneMore(Long orderId);
}
