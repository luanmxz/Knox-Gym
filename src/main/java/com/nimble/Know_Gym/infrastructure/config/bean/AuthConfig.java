package com.nimble.Know_Gym.infrastructure.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.nimble.Know_Gym.application.usecase.KeyCloakUseCase;

@Configuration
public class AuthConfig {

    @Bean
    KeyCloakUseCase keyCloakUseCase() {
        return new KeyCloakUseCase();
    }

}
