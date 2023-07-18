package com.socialmedia.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /**
     * Bu rabbitmq configuration sınıfı producer veya consumer işlemleri için gerekli alt yapıyı oluşturmayı sağlar.
     * Producer işlemi(bu serviste bir kuyruk üret ve verileri bu kuyruk üzerinden gönder) yapılacağında exchange, queue ve bindingKey
     * gibi değişkenlere ihtiyaç duyulur.
     * Ancak Consumer işlemi yapılacağı zaman yalnızca tüketilecek olan 'queue' oluşturularak kuyruktan gelen veri deserialize edilir.
     */
    private String exchange = "auth-exchange"; //tektir, yani her producer işlemi için birden çok oluşturmaya gerek yoktur.
    @Bean
    DirectExchange authExchange(){
        return new DirectExchange(exchange);
    }


    //User register producer
    private String userRegisterQueue = "user-register-queue"; //her producer işlemi için yeniden bir değişken oluşturulmalıdır.

    private String userRegisterBinding = "user-register-binding"; //her producer işlemi için yeniden bir değişken oluşturulmalıdır.

    @Bean
    Queue userRegisterQueue(){
        return new Queue(userRegisterQueue);
    }
    @Bean
    public Binding userRegisterBinding(final Queue userRegisterQueue, final DirectExchange authExchange){
        return BindingBuilder.bind(userRegisterQueue).to(authExchange).with(userRegisterBinding);
    }
}
