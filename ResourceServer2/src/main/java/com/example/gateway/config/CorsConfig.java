package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
//        configuration.addAllowedOrigin("http://localhost:8090");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }
//   WebMvcConfiguer을 Override 해서 사용하는 방법, 설정이 간결하지만, 의존코드에서 수정해서 사용 하는 것.
//   위 코드 CorsConfiguration 객체가 세밀한 설정 가능.
//    @Bean
//    WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:4200")
//                        .allowedMethods("*")
//                        .allowedHeaders("*");
//            }
//        };
//    }
}
