package com.vargas.leo.gerenciadorassembleia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GerenciadorAssembleiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GerenciadorAssembleiaApplication.class, args);
    }

}
