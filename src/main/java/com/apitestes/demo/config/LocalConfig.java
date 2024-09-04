package com.apitestes.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.apitestes.demo.entity.User;
import com.apitestes.demo.repositories.UserRepository;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository userRepository;
    
    @Bean
    public ApplicationRunner startDb() {
        return args -> {
            User user1 = new User(null, "lourivan", "lluz@gmail.com", "123");
            User user2 = new User(null, "adm", "adm@gmail.com", "123");

            userRepository.saveAll(List.of(user1, user2));
        };
    }
}
