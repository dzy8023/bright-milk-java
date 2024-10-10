package ncu.software;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement //开启注解方式的事务管理
@EnableScheduling // 启用定时任务
@SpringBootApplication
public class MilkServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MilkServerApplication.class, args);
    }

}