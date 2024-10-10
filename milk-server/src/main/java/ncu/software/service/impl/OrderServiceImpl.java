package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ncu.software.constant.AmountConstant;
import ncu.software.constant.StatusConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.MilkDTO;
import ncu.software.dto.OrderPageQueryDTO;
import ncu.software.entity.*;
import ncu.software.exception.BaseException;
import ncu.software.mapper.*;
import ncu.software.result.PageResult;
import ncu.software.service.OrderService;
import ncu.software.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MilkDetailMapper milkDetailMapper;

    /**
     * 订单支付
     *
     * @param id
     */
    @Transactional
    public void orderPayment(Long id) {
        //查询用户余额
        User user = userMapper.getById(BaseContext.getCurrentId());
        //查询订单金额
        Orders orders = orderMapper.getById(id);
        if (orders == null || !Objects.equals(orders.getUserId(), BaseContext.getCurrentId())) {
            throw new BaseException("订单不存在");
        }
        //获取订单状态
        if (!Objects.equals(orders.getStatus(), Orders.PENDING_PAYMENT)) {
            throw new BaseException("订单状态异常");
        }
        //判断余额是否足够
        if (user.getBalance().compareTo(orders.getDuePayment()) < 0) {
            throw new BaseException("余额不足");
        }
        //判断当前订单是否含有禁用商品,商品库存是否足够
        List<MilkDTO> milkDTOList = orderDetailMapper.getMilkDTOListByOID(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailList(id);
        List<MilkDetail> milkDetailList = new ArrayList<>();
        //将orderDetailList转换成hash表
        HashMap<Long, Integer> hashMap = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            hashMap.put(orderDetail.getMilkId(), orderDetail.getNumber());
        }
        for (MilkDTO milk : milkDTOList) {
            if (Objects.equals(milk.getStatus(), StatusConstant.DISABLE) || hashMap.get(milk.getId()) == null) {
                throw new BaseException("订单包含禁用商品不能购买");
            } else if (milk.getAmount() < hashMap.get(milk.getId())) {
                throw new BaseException("商品库存不足");
            }
            MilkDetail milkDetail = MilkDetail.builder()
                    .milkId(milk.getId())
                    .amount(milk.getAmount() - hashMap.get(milk.getId()))
                    .build();
            milkDetailList.add(milkDetail);
        }
        orders.setActualPayment(orders.getDuePayment());
        //扣款
        user.setBalance(user.getBalance().subtract(orders.getDuePayment()));
        //更新用户余额
        userMapper.updateBalanceById(user);
        //更新订单状态
        orders.setStatus(Orders.COMPLETED);
        orderMapper.updateStatus(orders);
        orderMapper.updateActualPayment(orders);
        //更新库存
        for (MilkDetail milkDetail : milkDetailList) {
            milkDetailMapper.updateAmountByMID(milkDetail);
        }
    }

    @Override
    public OrderVO getOrderById(Long id) {
        Orders order = orderMapper.getById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailMapper.getOrderDetailList(order.getId()));
        return orderVO;
    }

    @Override
    public void deleteOrderById(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null || !Objects.equals(order.getUserId(), BaseContext.getCurrentId())) {
            throw new BaseException("订单不存在");
        }
        if (Objects.equals(order.getStatus(), Orders.TO_BE_COMPLETED)) {
            throw new BaseException("订单待完成不能删除");
        }
        orderMapper.deleteById(id);
        orderDetailMapper.deleteBatch(id);
    }

    /**
     * 创建订单
     *
     * @return
     */
    @Transactional
    public OrderVO createOrder() {
        Long id = BaseContext.getCurrentId();
        //查询购物车
        List<ShoppingCart> shoppingCartsList = shoppingCartMapper.list(id);
        //插入一条订单数据
        Orders orders = Orders.builder()
                .userId(id)
                .number(String.valueOf(System.currentTimeMillis()))
                .status(Orders.PENDING_PAYMENT)
                //累加金额
                .duePayment(shoppingCartsList.stream().map(ShoppingCart::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                .actualPayment(AmountConstant.DEFAULT_BIGDECIMAL)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        //
        orderMapper.insert(orders);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        //插入订单详情
        for (ShoppingCart shoppingCart : shoppingCartsList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        //清空购物车
        shoppingCartMapper.deleteByUID(id);
        log.info("orderVO{}", orderVO);
        return orderVO;
    }


    /**
     * 分页查询订单
     *
     * @param orderPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(OrderPageQueryDTO orderPageQueryDTO) {
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.pageQuery(orderPageQueryDTO);
        List<Long> ids = page.getResult().stream().map(OrderVO::getId).toList();
        if (!ids.isEmpty()) {
            List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailListByOrderIds(ids);
            page.getResult().forEach(orderVO -> {
                orderVO.setOrderDetailList(orderDetailList.stream().filter(orderDetail -> Objects.equals(orderDetail.getOrderId(), orderVO.getId())).collect(toList()));
            });
        }
        return new PageResult(page.getTotal(), page.getResult());
    }
}

