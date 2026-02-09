package com.kukusha.users_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.kukusha.users_service",
        "com.kukusha.token_service",
        "com.kukusha.users_shared_lib"
})
@EnableJpaRepositories(basePackages = {
        "com.kukusha.users_shared_lib.repository",
        "com.kukusha.users_service"
})
@EntityScan(basePackages = {
        "com.kukusha.users_shared_lib",
        "com.kukusha.users_service"
})
public class UsersServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }
}
