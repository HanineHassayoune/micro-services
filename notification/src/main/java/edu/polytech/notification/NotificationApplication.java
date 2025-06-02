package edu.polytech.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        System.out.println("Notification service is starting...");
        SpringApplication.run(NotificationApplication.class, args);
    }

}
