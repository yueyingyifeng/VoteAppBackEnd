package com.fy.voteappbackend;

import com.fy.voteappbackend.WebMvcConfiguration.WebMvcConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@Import(WebMvcConfiguration.class)
@SpringBootApplication
public class VoteAppBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteAppBackEndApplication.class, args);
    }

}
