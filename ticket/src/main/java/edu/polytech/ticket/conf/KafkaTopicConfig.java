package edu.polytech.ticket.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // ← à importer
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ticketTopic() {
        return TopicBuilder.name("ticket-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
