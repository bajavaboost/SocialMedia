package com.socialmedia.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    //Auth register consumer
    private String mailRegisterQueue = "mail-register-queue";
    @Bean
    Queue mailRegisterQueue(){
        return new Queue(mailRegisterQueue);
    }
}
