package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ncu.software.constant.StatusConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.BookMilkOrderPageQueryDTO;
import ncu.software.dto.MilkDTO;
import ncu.software.entity.*;
import ncu.software.mapper.BookMilkMapper;
import ncu.software.mapper.BookMilkOrderMapper;
import ncu.software.mapper.MilkDetailMapper;
import ncu.software.mapper.OrderDetailMapper;
import ncu.software.result.PageResult;
import ncu.software.service.BookMilkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class BookMilkOrderServiceImpl implements BookMilkOrderService {
    @Autowired
    private BookMilkOrderMapper bookMilkOrderMapper;
    @Autowired
    private BookMilkMapper bookMilkMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private MilkDetailMapper milkDetailMapper;
    @Override
    public PageResult pageQuery(BookMilkOrderPageQueryDTO bookMilkOrderPageQueryDTO) {
        BookMilk bookMilk= BookMilk.builder()
                .userId(bookMilkOrderPageQueryDTO.getUserId())
                .status(bookMilkOrderPageQueryDTO.getStatus())
                .build();
        LocalDate day=LocalDate.now();
        if(bookMilkOrderPageQueryDTO.getDay()!=null){
            day=bookMilkOrderPageQueryDTO.getDay();
        }
        List<Long> bookMilkIds=bookMilkMapper.getIds(bookMilk);
        PageHelper.startPage(bookMilkOrderPageQueryDTO.getPage(), bookMilkOrderPageQueryDTO.getPageSize());
        Page<BookMilkOrder> page=bookMilkOrderMapper.pageQuery(bookMilkIds,day);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 完成订奶订单
     * @param id
     */
    @Override
    public void complete(Long id) {
       BookMilkOrder bookMilkOrder= bookMilkOrderMapper.getById(id);
        if(bookMilkOrder.getStatus()==BookMilkOrder.COMPLETED ||
                bookMilkOrder.getStatus()==BookMilkOrder.CANCELLED){
            throw new RuntimeException("订单已完成或已取消");
        }
        Long userId= BaseContext.getCurrentId();
        BookMilkOrder bmo=BookMilkOrder.builder()
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(userId)
                .build();
        if(bookMilkOrder.getStatus()==BookMilkOrder.TO_BE_COMPLETED||
            bookMilkOrder.getStatus()==BookMilkOrder.REPLENISH){
            //判断是否有库存
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
                    //该为补签
                    bmo.setStatus(BookMilkOrder.REPLENISH);
                    //更新状态
                    bookMilkOrderMapper.update(bmo);
                    throw new RuntimeException("订单包含禁用商品不能购买");
                } else if (milk.getAmount() < hashMap.get(milk.getId())) {
                    //取消订单
                    bmo.setStatus(BookMilkOrder.REPLENISH);
                    bookMilkOrderMapper.update(bmo);
                    throw new RuntimeException("商品库存不足");
                }
                MilkDetail milkDetail = MilkDetail.builder()
                        .milkId(milk.getId())
                        .amount(milk.getAmount() - hashMap.get(milk.getId()))
                        .build();
                milkDetailList.add(milkDetail);
            }
            //更新库存
            for(MilkDetail milkDetail:milkDetailList){
                milkDetailMapper.updateAmountByMID(milkDetail);
            }
        }
        bookMilkOrder.setStatus(BookMilkOrder.COMPLETED);
        bookMilkOrderMapper.update(bmo);
    }
}
