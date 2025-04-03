package com.woojhye.tasket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();     // 암호화
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth       // 스프링부트 3.1부터는 람다식 사용
                        .requestMatchers("/login", "/login-proc", "/sign-up", "/sign-up-proc").permitAll()     // 메인페이지와 로그인 페이지는 로그인 없이 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")                             // "/admin" 페이지는 관리자("ADMIN" 역할을 가진 사용자)만 접근 가능
                        .requestMatchers("/").hasAnyRole("ADMIN", "USER")           // "/"로 시작하는 페이지들은 "ADMIN" 또는 "USER" 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/css/**", "/js/**", "/image/**").permitAll()    //  정적 리소스 허용
                        .anyRequest().authenticated()       // 위에서 따로 설정하지 않은 모든 요청은 로그인한 사용자만 접근 가능
                );      //인가 설정

        http
                .formLogin((auth) -> auth.loginPage("/login")   // "/login" URL로 요청 시, 사용자가 만든 로그인 페이지 보여줌
                        .loginProcessingUrl("/login-proc")       // 로그인 폼을 제출하면 인증을 처리할 URL
                        .usernameParameter("email")   // username 대신 email을 사용
                        .passwordParameter("password")   // 비밀번호 파라미터 설정
                        .defaultSuccessUrl("/")   // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login?error")   // 로그인 실패 시 이동할 페이지
                        .failureHandler((request, response, exception) -> {
                            System.out.println("로그인 실패 원인: " + exception.getMessage());
                            response.sendRedirect("/login?error");
                        })
                        .permitAll()    //  로그인 페이지 및 로그인 요청에 대해 모든 사용자 접근 허용
                );

        http
                .logout((auth) -> auth.logoutUrl("/logout")     // 로그아웃 설정
                        .logoutSuccessUrl("/login"));

        http
                .sessionManagement((auth) -> auth   // 다중 로그인 설정
                        .maximumSessions(1)         // 하나의 아이디에 대한 다중 로그인 허용 개수 : 1개
                        .maxSessionsPreventsLogin(true));   // true : 초과시 새로운 로그인 차단. false : 초과시 기존 세션 하나 삭제

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());   // 로그인 시 동일한 세션에 대한 id 변경
                                                            // sessionFixation().none() : 로그인 시 세션 정보 변경 안함
                                                            // sessionFixation().newSession() : 로그인 시 세션 새로 생성

        return http.build();
    }

}