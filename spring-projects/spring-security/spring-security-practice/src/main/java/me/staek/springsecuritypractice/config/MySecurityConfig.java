package me.staek.springsecuritypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * spring-security 5부터는 WebSecurityConfigurerAdapter 사용안함
 * https://devlog-wjdrbs96.tistory.com/434
 */
@Configuration
public class MySecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                    authorize -> authorize.requestMatchers("/","info").permitAll()
                            .requestMatchers("/admin").hasRole("ADMIN")
                            .anyRequest().authenticated()
            );
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
