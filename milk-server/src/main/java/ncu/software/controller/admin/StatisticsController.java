package ncu.software.controller.admin;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ncu.software.result.Result;
import ncu.software.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/statistics")
@Slf4j
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/charts")
    public Result<Map<String, Object>> UserStatistics( @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(statisticsService.getStatistics(begin, end));
    }
    @GetMapping("/export/{type}")
    public void export(HttpServletResponse response, @PathVariable("type") String type, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        List<Integer> types= Arrays.stream(type.split(",")).map(Integer::parseInt).toList();
        statisticsService.exportBusinessData(response, types, begin, end);
    }
}