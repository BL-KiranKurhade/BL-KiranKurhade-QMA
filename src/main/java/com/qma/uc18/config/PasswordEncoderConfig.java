package com.qma.uc18.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Extracted into its own @Configuration so PasswordEncoder bean is created
 * independently — breaks the circular dependency:
 *   SecurityConfig -> CustomOAuth2UserService -> PasswordEncoder -> SecurityConfig
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
