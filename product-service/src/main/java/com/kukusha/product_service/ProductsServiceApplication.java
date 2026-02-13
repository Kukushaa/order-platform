package com.kukusha.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableFeignClients
@ComponentScan(basePackages = {
        "com.kukusha.product_service",
        "com.kukusha.token_service",
        "com.kukusha.kafka_messages_sender",
        "com.kukusha.users_shared_lib",
})
@EnableJpaRepositories(basePackages = {
        "com.kukusha.users_shared_lib.repository",
        "com.kukusha.product_service",
})
@EntityScan(basePackages = {
        "com.kukusha.users_shared_lib",
        "com.kukusha.product_service",
})
public class ProductsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApplication.class, args);
    }
}