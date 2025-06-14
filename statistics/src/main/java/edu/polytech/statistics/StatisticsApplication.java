package edu.polytech.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
public class StatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class, args);
    }

}
