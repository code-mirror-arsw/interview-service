package com.code_room.interview_service.infrastructure.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/services/be/interview-service/v3/api-docs/**",
                                "/services/be/interview-service/swagger-ui/**",
                                "/services/be/interview-service/swagger-ui.html",
                                "/services/be/interview-service/swagger-resources/**",
                                "/services/be/interview-service/webjars/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
