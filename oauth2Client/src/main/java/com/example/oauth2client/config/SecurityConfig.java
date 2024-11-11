package com.example.oauth2client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.springOAuth2.client-id}")
    String client_id;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, DefaultOAuth2AuthorizationRequestResolver resolver) throws Exception {

//        String base_uri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
//        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, base_uri);
//        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/css/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login
                        .loginPage("/oauth2/authorization/" + client_id)
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestResolver(resolver))
                )
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }
}
