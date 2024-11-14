package com.example.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ServerOAuth2AuthorizationRequestResolver pkceResolver) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/**").permitAll()
                        .anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(auth -> auth.authorizationRequestResolver(pkceResolver))
                .oauth2Client(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(serverLogoutSuccessHandler(clientRegistrationRepository)
                        ))
                .build();
    }

    @Bean
    ServerLogoutSuccessHandler serverLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/logged-out"); // authorization server post_logout_redirect_uris
        return oidcLogoutSuccessHandler;
    }

    @Bean
    ServerOAuth2AuthorizationRequestResolver pkceResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {

        // DefaultOAuth2AuthorizationRequestResolver
        // DefaultServerOAuth2AuthorizationRequestResolver
        DefaultServerOAuth2AuthorizationRequestResolver resolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }

}
