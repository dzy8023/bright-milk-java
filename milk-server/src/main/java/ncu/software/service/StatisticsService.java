package ncu.software.service;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticsService {

    Map<String, Object> getStatistics(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response, List<Integer> types, LocalDate begin, LocalDate end);
}
