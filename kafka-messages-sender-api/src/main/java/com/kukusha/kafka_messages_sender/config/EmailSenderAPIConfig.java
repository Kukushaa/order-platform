package com.kukusha.kafka_messages_sender.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.kukusha.kafka_messages_sender")
@PropertySource("classpath:application.yml")
public class EmailSenderAPIConfig {
}
