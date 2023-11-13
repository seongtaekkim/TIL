package me.staek.springsecurityjwt.config;

import me.staek.springsecurityjwt.CorsConfig;
import me.staek.springsecurityjwt.account.AccountRepository;
import me.staek.springsecurityjwt.jwt.JwtAuthenticationFilter;
import me.staek.springsecurityjwt.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// https://github.com/spring-projects/spring-security/issues/10822 참고
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

	@Autowired
	private AccountRepository userRepository;

	@Autowired
	private CorsConfig corsConfig;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable() // stateless 기반 인증에서는 필요없는 토큰설정.(Cross site Request forgery)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().disable()
				.httpBasic().disable()
				.apply(new MyCustomDsl()) // 커스텀 필터 등록
				.and()
				.authorizeRequests(authroize ->
						authroize.antMatchers("/user")
						.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
						.antMatchers("/admin")
						.access("hasRole('ROLE_ADMIN')")
								.antMatchers("/","/account/**").permitAll()
						.anyRequest().permitAll())
//				.formLogin().and().httpBasic().and()
				.build();
	}

	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http
					.addFilter(corsConfig.corsFilter())
					.addFilter(new JwtAuthenticationFilter(authenticationManager))
					.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
		}
	}

}
