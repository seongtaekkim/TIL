package me.staek.springsecurityjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * spring-security 5부터는 WebSecurityConfigurerAdapter 사용안함
 * https://devlog-wjdrbs96.tistory.com/434
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class MySecurityConfig {

//    @Bean
//    public SecurityExpressionHandler expressionHandler() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//
//        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
//        handler.setRoleHierarchy(roleHierarchy);
//
//        return handler;
//    }

//    @Bean
//    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
//        var access = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("USER");
//        var hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//        access.setRoleHierarchy(hierarchy);
//
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().access(access));
//
//        return http.build();
//
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        /**
//         * ADMIN 유저는 USER role 을 포함한다는 계층구조를 정의
//         */
//        var access = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("USER");
//        var hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//        access.setRoleHierarchy(hierarchy);
//
//        http
//            .authorizeHttpRequests(
//                    authorize -> authorize.requestMatchers("/","info", "/account/**").permitAll()
//                            .requestMatchers("/admin").hasRole("ADMIN")
//                            .requestMatchers("/user").access(access) // 계층구조 설정 추가
//                            .anyRequest().authenticated()
//
//            );
//
//        http.formLogin();
//        http.httpBasic();
//
//        return http.build();
//    }


    /**
     * spring-security 5.7 미만에서 사용
     */
    // 기본 패스워드 인코더 ${noop}
//    public AuthenticationManager filterChain2(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("staek").password("${noop}123").roles("USER").and()
//                .withUser("admin").password("${noop}123").roles("ADMIN");
//        return auth.build();
//    }

    /**
     * 초기 유저 자동생성 커스텀 로직
     * - inmemory 에 하드코드로 작성하는 것 자체가 문제가 있음.
     */
    /*
    @Bean
    public UserDetailsManager users() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123")
                .roles("ADMIN")
                .build();
        InMemoryUserDetailsManager in = new InMemoryUserDetailsManager();
        in.createUser(user);
        in.createUser(admin);
        return in;
    }
     */

}
