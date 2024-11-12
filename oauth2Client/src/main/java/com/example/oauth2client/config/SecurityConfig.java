package com.example.oauth2client.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2Properties oAuth2Properties;

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DefaultOAuth2AuthorizationRequestResolver resolver,
            LogoutSuccessHandler oidcLogoutSuccessHandler) throws Exception {

        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/index", "/css/**", "/favicon.ico", "/logout", "/logged-out", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login
                        .loginPage(oAuth2Properties.getLoginUrl())
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestResolver(resolver))
//                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
//                                .oidcUserService(oidcUserService()))
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler)
                )
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    DefaultOAuth2AuthorizationRequestResolver resolver(ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        );
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }

    @Bean
    LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidcClientInitiatedLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/logged-out");
        return oidcClientInitiatedLogoutSuccessHandler;
    }
}
