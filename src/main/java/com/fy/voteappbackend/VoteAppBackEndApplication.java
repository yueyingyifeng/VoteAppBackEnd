package com.fy.voteappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VoteAppBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteAppBackEndApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerCustomizer() {
        return factory -> factory.setPort(8081);
    }
}
