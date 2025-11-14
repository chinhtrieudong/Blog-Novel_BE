package com.blognovel.blognovel.config;

import com.blognovel.blognovel.jwt.JwtAuthenticationFilter;
import com.blognovel.blognovel.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CorsConfigurationSource corsConfigurationSource;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        CorsConfigurationSource corsConfigurationSource) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.corsConfigurationSource = corsConfigurationSource;
        }

        private static final String[] PUBLIC_ENDPOINTS = {
                        "/api/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/api/uploads/**",
                        "/api/novels/*/views",
                        "/api/novels/*/chapters/*/views",
                        "/api/posts/*/views"
        };

        private static final String[] PUBLIC_GET_ENDPOINTS = {
                        "/api/posts/**",
                        "/api/novels/**",
                        "/api/search/**",
                        "/api/posts/*/comments",
                        "/api/novels/*/comments",
                        "/api/novels/*/chapters/**",
                        "/api/novels/genres",
                        "/api/categories/**",
                        "/api/genres/**",
                        "/api/users"
        };

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
                return new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException authException) throws IOException {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                                final ObjectMapper mapper = new ObjectMapper();
                                mapper.writeValue(response.getOutputStream(),
                                                new ErrorResponse(401, "Authentication failed: "
                                                                + authException.getMessage()));
                        }
                };
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
