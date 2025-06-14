package edu.polytech.statistics.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "notifications")
public interface NotificationFeignClient {
}
