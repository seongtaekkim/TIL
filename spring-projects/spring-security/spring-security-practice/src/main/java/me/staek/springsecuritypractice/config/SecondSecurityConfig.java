package me.staek.springsecuritypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 15)
public class SecondSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers("/account/**").permitAll()
                );
        return http.build();
    }

}
