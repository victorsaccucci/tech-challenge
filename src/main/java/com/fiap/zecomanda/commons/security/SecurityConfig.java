package com.fiap.zecomanda.commons.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults()).sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs*/**", "/swagger-ui.html").permitAll().requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll().requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                // change-password e /user **PROTEGIDOS**
                .anyRequest().authenticated()).exceptionHandling(ex -> ex
                // SEM token / token inválido -> 401
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(401);
                    res.setContentType("application/json");
                    res.getWriter().write("""
                                {"timestamp":"%s","status":401,"error":"Unauthorized",
                                 "message":"Authentication required.","path":"%s"}
                            """.formatted(java.time.LocalDateTime.now(), req.getRequestURI()));
                })
                // Token VÁLIDO mas sem permissão/role -> 403
                .accessDeniedHandler((req, res, e) -> {
                    res.setStatus(403);
                    res.setContentType("application/json");
                    res.getWriter().write("""
                                {"timestamp":"%s","status":403,"error":"Forbidden",
                                 "message":"Access is denied.","path":"%s"}
                            """.formatted(java.time.LocalDateTime.now(), req.getRequestURI()));
                })).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}