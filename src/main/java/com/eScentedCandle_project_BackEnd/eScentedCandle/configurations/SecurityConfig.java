package com.eScentedCandle_project_BackEnd.eScentedCandle.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Role.ADMIN;
import static com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/user",
                                "api/v1/payment/**",
                                "/api/v1/user/login",
                                "api/v1//user/verify-account",
                                "/api/v1/user/forgot-password",
                                "/api/v1/user/reset-password"
                        ).permitAll()
                        .requestMatchers(PATCH,"api/v1/order/**").hasAuthority(ADMIN.name())
                        .requestMatchers("api/v1/feedback/**").hasAuthority(USER.name())
                        .requestMatchers("api/v1/reply").hasAuthority(ADMIN.name())
                        .requestMatchers(POST,"api/v1/order/**").hasAuthority(USER.name())
                        .requestMatchers(PUT,
                                "api/v1/user/**").hasAnyAuthority(ADMIN.name(),USER.name())
                        .requestMatchers("api/v1/address/**").hasAnyAuthority(USER.name(),ADMIN.name())
                        .requestMatchers(DELETE,
                                "api/v1/category/**",
                                "api/v1/products/**,api/v1/category_type/**",
                                "api/v1/user/**").hasAuthority(ADMIN.name())
                        .requestMatchers(POST,
                                "api/v1/category/**",
                                "api/v1/products/**",
                                "api/v1/category_type/**",
                                "api/v1/user/**").hasAuthority(ADMIN.name())
                        .requestMatchers(PUT,"api/v1/category/**",
                                "api/v1/products/**",
                                "api/v1/category_type/**").hasAuthority(ADMIN.name())
                        .requestMatchers(GET, "/api/v1/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/user/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()));
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}