package com.woojhye.tasket.config;

import com.woojhye.tasket.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(daoAuthenticationProvider()) // ← 이거 하나만 있으면 충분!

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/login-proc", "/sign-up", "/sign-up-proc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/", "/home").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/css/**", "/js/**", "/image/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/login-proc")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .failureHandler((request, response, exception) -> {
                            System.out.println("❌ 로그인 실패 원인: " + exception.getMessage());
                            System.out.println("입력한 이메일: " + request.getParameter("email"));
                            System.out.println("입력한 비밀번호: " + request.getParameter("password"));
                            response.sendRedirect("/login?error");
                        })
                        .permitAll()
                )
                .logout(auth -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )
                .sessionManagement(auth -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )
                .authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }
    @Bean
    CommandLineRunner testEncoding(BCryptPasswordEncoder encoder) {
        return args -> {
            String raw = "test10";
            String encoded = "$2a$10$eDKdNKkSOp46JvyzGFUjxuYMyXiYJYmdzYOziquXWs0y.ycAUlP9O";

            System.out.println("비밀번호 매치 여부: " + encoder.matches(raw, encoded));
        };
    }
}