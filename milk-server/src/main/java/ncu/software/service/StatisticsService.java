package ncu.software.service;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {

    Map<String, Object> getStatistics(LocalDate begin, LocalDate end);
}
