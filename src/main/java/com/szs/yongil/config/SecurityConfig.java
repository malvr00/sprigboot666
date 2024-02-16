package com.szs.yongil.config;

import com.szs.yongil.common.jwt.TokenProvider;
import com.szs.yongil.common.jwt.JwtAccessDeniedHandler;
import com.szs.yongil.common.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final TokenProvider tokenProvider;

    /**
     * Security setting bean
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // JWT 토큰을 사용하는 방식이기 때문에 csrf와 sesstion을 사용하지 않습니다.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 토큰 ERROR handler
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                // H2 웹 콘솔의 iframe이 정삭작동 하기 위해 X-Frame-Options header 값 SAMEORIGIN로 변경
                // 브라우저의 모든 iframe에 대한 모든 요청을 허용하면 디도스 공격 등 취약해 질 수 있다.
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // Method 제어
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/szs/signup").permitAll()
                                .requestMatchers("/szs/login").permitAll()
                                .requestMatchers("/error/**").permitAll()
                                .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .anyRequest().authenticated()
                )

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {})


                .build();
    }

}
