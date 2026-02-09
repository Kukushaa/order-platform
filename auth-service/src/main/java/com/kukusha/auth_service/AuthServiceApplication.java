package com.kukusha.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.kukusha.auth_service",
        "com.kukusha.token_service",
        "com.kukusha.kafka_messages_sender",
        "com.kukusha.users_shared_lib",
})
@EnableJpaRepositories(basePackages = {
        "com.kukusha.users_shared_lib.repository",
        "com.kukusha.auth_service"
})
@EntityScan(basePackages = {
        "com.kukusha.users_shared_lib",
        "com.kukusha.auth_service"
})
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
