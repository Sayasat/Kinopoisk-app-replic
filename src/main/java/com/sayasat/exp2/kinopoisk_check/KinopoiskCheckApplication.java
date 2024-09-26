package com.sayasat.exp2.kinopoisk_check;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KinopoiskCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinopoiskCheckApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
