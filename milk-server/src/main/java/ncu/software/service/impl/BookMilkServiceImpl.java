package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ncu.software.constant.AmountConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.BookMilkPageQueryDTO;
import ncu.software.entity.*;
import ncu.software.mapper.*;
import ncu.software.result.PageResult;
import ncu.software.service.BookMilkService;
import ncu.software.vo.BookMilkOrderVO;
import ncu.software.vo.BookMilkPageVO;
import ncu.software.vo.BookMilkVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookMilkServiceImpl implements BookMilkService {
    @Autowired
    private BookMilkMapper bookMilkMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private BookMilkOrderMapper bookMilkOrderMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    /**
     * 分页查询
     * @param bookMilkPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(BookMilkPageQueryDTO bookMilkPageQueryDTO) {
        PageHelper.startPage(bookMilkPageQueryDTO.getPage(), bookMilkPageQueryDTO.getPageSize());
        Page<BookMilkPageVO>page=bookMilkMapper.pageQuery(bookMilkPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 创建订奶表
     * @return
     */
    @Override
    public BookMilkOrderVO createBookMilk(LocalDate startTime, LocalDate endTime,Integer type) {
       int days=getDays(startTime,endTime);
       BigDecimal amount=BigDecimal.ZERO;
        Long id = BaseContext.getCurrentId();
        //查询购物车
        List<ShoppingCart> shoppingCartsList = shoppingCartMapper.list(id);
        for (ShoppingCart shoppingCart : shoppingCartsList){
            //累加金额
            amount=amount.add(shoppingCart.getAmount()).multiply(BigDecimal.valueOf(days));
        }
        //插入一条订单数据
        Orders orders = Orders.builder()
                .userId(id)
                .number(String.valueOf(System.currentTimeMillis()))
                .status(Orders.PENDING_PAYMENT)
                //累加金额
                .duePayment(amount)
                .actualPayment(BigDecimal.ZERO)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        //
        orderMapper.insert(orders);
        //插入订奶表
        BookMilk bookMilk = BookMilk.builder()
                .userId(id)
                .orderId(orders.getId())
                .type(type)
                .status(BookMilk.PENDING_PAYMENT)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        //
        bookMilkMapper.insert(bookMilk);
        //插入订奶订单表
        BookMilkOrder bookMilkOrder = BookMilkOrder.builder()
                .bookId(bookMilk.getId())
                .status(BookMilkOrder.TO_BE_COMPLETED)
                .updateTime(LocalDateTime.now())
                .build();
        bookMilkOrderMapper.insert(bookMilkOrder);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //插入订单详情
        for (ShoppingCart shoppingCart : shoppingCartsList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        BookMilkOrderVO bookMilkOrderVO = new BookMilkOrderVO();
        BeanUtils.copyProperties(orders, bookMilkOrderVO);
        bookMilkOrderVO.setId(bookMilk.getId());
        bookMilkOrderVO.setStartTime(bookMilk.getStartTime());
        bookMilkOrderVO.setEndTime(bookMilk.getEndTime());
        bookMilkOrderVO.setMilkDetailList(orderDetailList);
        //清空购物车
        shoppingCartMapper.deleteByUID(id);
        return bookMilkOrderVO;
    }

    /**
     * 订奶支付id为bookMilkId
     * @param id
     */
    @Override
    public void bookMilkPayment(Long id) {
     BookMilk bookMilk= bookMilkMapper.getById(id);
        if(bookMilk==null || bookMilk.getOrderId()==null){
            throw new RuntimeException("订奶支付失败");
        }
        //调用支付
        orderServiceImpl.orderPayment(bookMilk.getOrderId());
        //更新订奶状态
        BookMilkOrder bookMilkOrder = BookMilkOrder.builder()
                .bookId(bookMilk.getId())
                .status(BookMilkOrder.OUT_OF_STOCK)
                .updateTime(LocalDateTime.now())
                .build();
        bookMilkOrderMapper.update(bookMilkOrder);
    }

    /**
     * 获取订奶详细信息
     * @param id
     * @return
     */
    @Override
    public BookMilkVO getBookMilk(Long id) {
        BookMilk bookMilk=bookMilkMapper.getById(id);
        if(bookMilk==null || bookMilk.getOrderId()==null){
            throw new RuntimeException("获取订奶详细信息失败");
        }
        Orders orders=orderMapper.getById(bookMilk.getOrderId());
        List<OrderDetail> orderDetailList=orderDetailMapper.getOrderDetailList(bookMilk.getOrderId());
        List<BookMilkOrder> bookMilkOrderList=bookMilkOrderMapper.getBookMilkOrderList(bookMilk.getId());
        return BookMilkVO.builder()
                .id(bookMilk.getId())
                .number(orders.getNumber())
                .type(bookMilk.getType())
                .status(bookMilk.getStatus())
                .startTime(bookMilk.getStartTime())
                .endTime(bookMilk.getEndTime())
                .bookMilkOrderList(bookMilkOrderList)
                .milkDetailList(orderDetailList)
                .build();
    }
    //获取日期列表
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        return dateList;
    }

    private int getDays(LocalDate begin, LocalDate end){
        int days=0;
        while (!begin.isAfter(end)) {
            days++;
            begin = begin.plusDays(1);
        }
        return days;
    }

}
