package com.socialmedia.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    //Mail sender register producer
    private String mailRegisterQueue = "mail-register-queue";
    private String mailRegisterBinding = "mail-register-binding";

    @Bean
    Queue mailRegisterQueue(){
        return new Queue(mailRegisterQueue);
    }
    @Bean
    public Binding mailRegisterBinding(final Queue mailRegisterQueue, final DirectExchange authExchange){
        return BindingBuilder.bind(mailRegisterQueue).to(authExchange).with(mailRegisterBinding);
    }

    //User forgot password
    private String forgotPassQueue = "forgot-pass-queue";
    @Bean
    Queue forgotPassQueue(){
        return new Queue(forgotPassQueue);
    }

    private String forgotPassBinding = "forgot-pass-binding";

    @Bean
    public Binding forgotPassBinding(final Queue forgotPassQueue, final DirectExchange authExchange){
        return BindingBuilder.bind(forgotPassQueue).to(authExchange).with(forgotPassBinding);
    }
}
