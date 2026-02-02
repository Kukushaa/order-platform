package com.kukusha.emailsenderapi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.kukusha.emailsenderapi")
@PropertySource("classpath:application.yml")
public class EmailSenderAPIConfig {
}
