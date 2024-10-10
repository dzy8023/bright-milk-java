package ncu.software.service.impl;

import lombok.extern.slf4j.Slf4j;
import ncu.software.entity.BookMilk;
import ncu.software.entity.BookMilkOrder;
import ncu.software.mapper.BookMilkMapper;
import ncu.software.mapper.BookMilkOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Component
@Slf4j
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Autowired
    private BookMilkOrderMapper bookMilkOrderMapper;
    @Autowired
    private BookMilkMapper bookMilkMapper;
    @Override
    public void run(String... args) throws Exception {
       //更新订奶订单表
        log.info("更新订奶订单表");
        List<Long>ids=bookMilkOrderMapper.getIdsByStatus(BookMilkOrder.REPLENISH);
        List<Long>bmIds=bookMilkMapper.getIdsByStatus(BookMilk.TO_BE_COMPLETED);
        LocalDate day=LocalDate.now();
        BookMilkOrder bookMilkOrder= BookMilkOrder.builder()
                .updateTime(LocalDateTime.now())
                .build();
        //获取之前订奶订单状态为TO_BE_COMPLETED的数据
        List<BookMilkOrder>bookMilkOrderList=bookMilkOrderMapper.getIdsByStatusBefore(BookMilkOrder.TO_BE_COMPLETED,day);
        for (BookMilkOrder b : bookMilkOrderList) {
            if (!bmIds.contains(b.getBookId())){
                bookMilkOrder.setId(b.getId());
                bookMilkOrder.setStatus(BookMilkOrder.CANCELLED);
                //取消订单
                bookMilkOrderMapper.update(bookMilkOrder);
            }else {
                ids.add(b.getId());
            }
        }
        //插入今天的订奶订单数据
        for (Long bmId : bmIds) {
           List<BookMilkOrder>bookMilkOrders=bookMilkOrderMapper.getByBIdDay(bmId,day);
            if(bookMilkOrders.isEmpty()){
                //插入一条数据
                bookMilkOrder.setBookId(bmId);
                bookMilkOrder.setStatus(BookMilkOrder.TO_BE_COMPLETED);
                bookMilkOrderMapper.insert(bookMilkOrder);
            }
        }
        //更新之前订奶订单状态为TO_BE_COMPLETED，REPLENISH的数据
        if(!ids.isEmpty()){
            bookMilkOrderMapper.updateStatusByIds(ids,BookMilkOrder.TO_BE_COMPLETED, LocalDateTime.now());
            log.info("更新订奶订单表成功----更新了{}条数据",ids.size());
        }
        //首先查询所有订奶状态为TO_BE_COMPLETED，并且订奶订单表状态为COMPLETED的数据
        //然后判断是否完成，如果完成就需要将其它都变为CANCELLED
        List<BookMilk>bookMilkList=bookMilkMapper.getByStatus(BookMilk.TO_BE_COMPLETED);
        for (BookMilk b : bookMilkList) {
            List<Long>completedIds=bookMilkOrderMapper.getIdsByStatus(BookMilkOrder.COMPLETED);
            //判断是否完成
            int days=0;
            days=getDays(b.getStartTime(),b.getEndTime());
            List<Long>toBeCompletedIds=bookMilkOrderMapper.getIdsByStatus(BookMilkOrder.TO_BE_COMPLETED);
            if(completedIds.size()<days){
                int index=completedIds.size()+toBeCompletedIds.size()-days;
                //多了
                if(index>0){
                   for(int i=0;i<index;i++){
                       //将多出来的取消
                       bookMilkOrder.setId(toBeCompletedIds.get(i));
                       bookMilkOrder.setStatus(BookMilkOrder.CANCELLED);
                       bookMilkOrderMapper.update(bookMilkOrder);
                   }
                }
            }else {
                //将其它状态为TO_BE_COMPLETED的变为CANCELLED
                if(!toBeCompletedIds.isEmpty()){
                    bookMilkOrderMapper.updateStatusByIds(toBeCompletedIds,BookMilkOrder.CANCELLED, LocalDateTime.now());
                }
            }
        }

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
