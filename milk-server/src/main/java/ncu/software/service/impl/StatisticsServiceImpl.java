package ncu.software.service.impl;

import lombok.extern.slf4j.Slf4j;
import ncu.software.mapper.OrderMapper;
import ncu.software.mapper.UserMapper;
import ncu.software.service.StatisticsService;
import ncu.software.vo.OrderBasicDetailVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, Object> getStatistics(LocalDate begin, LocalDate end) {
        log.info("getStatistics begin:{}, end:{}", begin, end);
        LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);

        Map<String, Object> result = new HashMap<>();
        //查询营业额
        List<HashMap<String,Double>> turnoverList = orderMapper.getTurnoverStatistics(beginDateTime, endDateTime);
       List<HashMap<String,Integer>>newUserList = userMapper.getNewUserStatistics(beginDateTime, endDateTime);
       List<HashMap<String,Integer>>orderList=orderMapper.getOrderStatistics(beginDateTime, endDateTime);
       List<OrderBasicDetailVO>saleList=orderMapper.getSaleStatistics(beginDateTime, endDateTime);
        result.put("revenueData", turnoverList);
        result.put("newUserData", newUserList);
        result.put("orderData", orderList);
        result.put("saleData", saleList);
        result.put("totalUserData", userMapper.countUserBefore(beginDateTime));
        return result;
    }

    private List<LocalDate> getDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        return dateList;
    }












}
