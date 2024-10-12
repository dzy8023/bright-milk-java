package ncu.software.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ncu.software.mapper.OrderMapper;
import ncu.software.mapper.UserMapper;
import ncu.software.service.StatisticsService;
import ncu.software.vo.NewUserVO;
import ncu.software.vo.OrderBasicDetailVO;
import ncu.software.vo.OrderOfOneDayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        List<HashMap<String, Double>> turnoverList = orderMapper.getTurnoverStatistics(beginDateTime, endDateTime);
        List<NewUserVO> newUserList = userMapper.getNewUserStatistics(beginDateTime, endDateTime);
        List<HashMap<String, Integer>> orderList = orderMapper.getOrderStatistics(beginDateTime, endDateTime);
        List<OrderBasicDetailVO> saleList = orderMapper.getSaleStatistics(beginDateTime, endDateTime);
        result.put("revenueData", turnoverList);
        result.put("newUserData", newUserList);
        result.put("orderData", orderList);
        result.put("saleData", saleList);
        result.put("totalUserData", userMapper.countUserBefore(beginDateTime));
        return result;
    }

    @Override
    public void exportBusinessData(HttpServletResponse response, List<Integer> types, LocalDate begin, LocalDate end) {
        log.info("exportBusinessData types:{}, begin:{}, end:{}", types, begin, end);
        LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);
        Map<String, Object> data = formatData(beginDateTime, endDateTime);
        List<Sales> sales = (List<Sales>) data.get("sales");
        Header header = (Header) data.get("header");
        List<Detail> details = (List<Detail>) data.get("details");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String templatePath = "/templates/运营数据报表模板.xlsx";
        URL resource = this.getClass().getResource(templatePath);
        if (resource != null) {
            // 这里 需要指定写用哪个class去写
            String path = URLDecoder.decode(resource.getPath(), StandardCharsets.UTF_8);
            String fileName = URLEncoder.encode("运营数据报表.xlsx", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(path).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                excelWriter.fill(details, writeSheet);
                excelWriter.fill(sales, writeSheet);
                excelWriter.fill(header, writeSheet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.error("找不到模板文件");
            throw new RuntimeException("找不到模板文件");
        }
    }

    private List<LocalDate> getDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        return dateList;
    }

    private HashMap<String, Object> formatData(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        List<OrderOfOneDayVO> turnoverList = orderMapper.getOrderOfOneDayStatistics(beginDateTime, endDateTime);
        List<NewUserVO> newUserList = userMapper.getNewUserStatistics(beginDateTime, endDateTime);
        List<OrderBasicDetailVO> saleList = orderMapper.getSaleStatistics(beginDateTime, endDateTime);
        List<Sales> sales = new ArrayList<>(saleList.stream().map(item -> new Sales(item.getName(), item.getNumber())).toList());
        List<Detail> details = new ArrayList<>();
        //降序排列
        turnoverList.sort(Comparator.comparing(OrderOfOneDayVO::getDate).reversed());
        newUserList.sort(Comparator.comparing(NewUserVO::getDate).reversed());
        sales.sort(Comparator.comparing(Sales::getSales).reversed());
        for (int i = turnoverList.size() - 1, j = newUserList.size() - 1; i >= 0 || j >= 0; ) {
            if (i >= 0 && j >= 0) {
                NewUserVO newUserVO = newUserList.get(j);
                OrderOfOneDayVO ofOneDayVO = turnoverList.get(i);
                Double completion = safeDivide(ofOneDayVO.getValidOrder(), ofOneDayVO.getTotalOrder(), Double.class);
                if (ofOneDayVO.getDate().compareTo(newUserVO.getDate()) < 0) {
                    BigDecimal avgPrice = safeDivide(ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), BigDecimal.class);
                    details.add(new Detail(ofOneDayVO.getDate(), ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), completion, avgPrice, 0));
                    i--;
                } else if (ofOneDayVO.getDate().compareTo(newUserVO.getDate()) == 0) {
                    BigDecimal avgPrice = safeDivide(ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), BigDecimal.class);
                    details.add(new Detail(ofOneDayVO.getDate(), ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), completion, avgPrice, newUserVO.getNewUser()));
                    i--;
                    j--;
                } else {
                    details.add(new Detail(newUserVO.getDate(), BigDecimal.ZERO, 0, completion, BigDecimal.ZERO, newUserVO.getNewUser()));
                    j--;
                }
            } else if (j < 0) {
                OrderOfOneDayVO ofOneDayVO = turnoverList.get(i);
                Double completion =safeDivide(ofOneDayVO.getValidOrder(), ofOneDayVO.getTotalOrder(), Double.class);
                BigDecimal avgPrice = safeDivide(ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), BigDecimal.class);
                details.add(new Detail(ofOneDayVO.getDate(), ofOneDayVO.getTurnover(), ofOneDayVO.getValidOrder(), completion, avgPrice, 0));
                i--;
            } else {
                NewUserVO newUserVO = newUserList.get(j);
                details.add(new Detail(newUserVO.getDate(), BigDecimal.ZERO, 0, 0.0, BigDecimal.ZERO, newUserVO.getNewUser()));
                j--;
            }
        }
        BigDecimal turnover = turnoverList.stream().map(OrderOfOneDayVO::getTurnover).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer validOrder = turnoverList.stream().map(OrderOfOneDayVO::getValidOrder).reduce(0, Integer::sum);
        Integer totalOrder = turnoverList.stream().map(OrderOfOneDayVO::getTotalOrder).reduce(0, Integer::sum);
        Double completion = safeDivide(validOrder, totalOrder, Double.class);
        BigDecimal avgPrice =safeDivide(turnover, validOrder, BigDecimal.class);
        Header header = Header.builder()
                .rangeDate(beginDateTime.toLocalDate().toString() + "至" + endDateTime.toLocalDate().toString())
                .turnover(turnover)
                .completion(completion)
                .totalUser(userMapper.countUserBefore(endDateTime))
                .avgPrice(avgPrice)
                .validOrder(validOrder)
                .newUser(newUserList.stream().mapToInt(NewUserVO::getNewUser).sum())
                .build();
        HashMap<String, Object> result = new HashMap<>();
        result.put("details", details);
        result.put("sales", sales);
        result.put("header", header);
        return result;
    }
    private <T extends Number, V extends Number,R> R safeDivide(T numerator, V denominator,Class<R> clazz) {
        // 如果 numerator 是 BigDecimal 类型
        if (clazz== Double.class) {
            return clazz.cast(denominator.doubleValue() == 0 ? 0.0 : numerator.doubleValue() / denominator.doubleValue());
        }else if (clazz== BigDecimal.class) {
            return clazz.cast(denominator.doubleValue() == 0 ? BigDecimal.ZERO : (((BigDecimal) numerator).divide(BigDecimal.valueOf(denominator.doubleValue()), RoundingMode.HALF_UP)));
        }else {
            return clazz.cast(numerator.doubleValue() / denominator.doubleValue());
        }
    }
}

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
class Header {
    String rangeDate;
    private BigDecimal turnover;
    private Double completion;
    private Integer totalUser;
    private BigDecimal avgPrice;
    private Integer validOrder;
    private Integer newUser;
}

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
class Detail {
    private String date;
    private BigDecimal turnover;
    private Integer vOrder;
    private Double cOrder;
    private BigDecimal price;
    private Integer newUser;
}

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
class Sales {
    String name;
    private Integer sales;
}

