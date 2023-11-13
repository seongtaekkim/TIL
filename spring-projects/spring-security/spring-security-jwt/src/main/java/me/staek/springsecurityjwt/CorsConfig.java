package me.staek.springsecurityjwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true); // js 가 password 처리 가능여부
      config.addAllowedOrigin("*"); // ip all
      config.addAllowedHeader("*"); // header all
      config.addAllowedMethod("*"); // method all

      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }

}