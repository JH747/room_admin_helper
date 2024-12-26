//package com.example.backend_spring;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
//
//import java.util.Arrays;
//
//@Configuration
//public class SecurityFilterChainConfig {
//
//    private final AuthenticationProvider authenticationProvider;
//    private final JWTAuthenticationFilter jwtAuthenticationFilter;
//
//    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider, JWTAuthenticationFilter jwtAuthenticationFilter) {
//        this.authenticationProvider = authenticationProvider;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize
//
//                        .requestMatchers("/test/**").permitAll()
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")  // /admin 경로는 ROLE_ADMIN만 접근 가능
//                        .requestMatchers("/actuator/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//
//                )
//                .authenticationProvider(authenticationProvider)
////                .addFilterBefore(jwtAuthenticationFilter, ExceptionTranslationFilter.class) // does not wowrk
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
////        http.setSharedObject(AsyncSupportConfigurer.class, new AsyncSupportConfigurer().setDefaultTimeout(5*60*1000)); // does not work
//
//        return http.build();
//    }
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 오리진 허용
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 Origin
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드
//        configuration.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더
//        configuration.setAllowCredentials(true); // 인증 정보 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
//        return source;
//    }
//}
