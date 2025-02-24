package com.gameaffinity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.gameaffinity.repository") // 👈 Agregar esta línea
public class GameAffinityApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameAffinityApplication.class, args);
    }
}

