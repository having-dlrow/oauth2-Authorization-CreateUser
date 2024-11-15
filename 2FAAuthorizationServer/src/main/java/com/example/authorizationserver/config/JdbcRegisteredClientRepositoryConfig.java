package com.example.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Configuration
public class JdbcRegisteredClientRepositoryConfig {
//    @Bean
//    RegisteredClientRepository jdbcRegisteredClientRepository(JdbcTemplate template) {
//        return new JdbcRegisteredClientRepository(template);
//    }

    @Bean
    RegisteredClientRepository inMemoryRegisteredClientRepository() {
        String clientId = "youtube-lecture-oauth2";
        String clientSecret = "{bcrypt}$2a$14$k4M/IICUdwmeTk0/nByDqee/dZ3YRPK6KlHHqEcIKUfVZR3R8.AX6";

        return new InMemoryRegisteredClientRepository(RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientName(clientId)
                .clientIdIssuedAt(Instant.now())
                .clientSecretExpiresAt(Instant.MAX)
                .clientAuthenticationMethods(methods -> {
                    methods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    methods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantTypes(types -> {
                    types.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    types.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    types.add(AuthorizationGrantType.REFRESH_TOKEN);
                    types.add(new AuthorizationGrantType("custom_password"));
                })
                .redirectUri("http://127.0.0.1:8090/login/oauth2/code/youtube-lecture-oauth2")
                .redirectUri("http://127.0.0.1:8082/login/oauth2/code/youtube-lecture-oauth2")
                .redirectUri("https://oauthdebugger.com/debug")
                .redirectUri("https://oidcdebugger.com/debug")
                .postLogoutRedirectUri("http://127.0.0.1:8082/logged-out")
                .scopes(scope -> {
                    scope.add(OidcScopes.OPENID);
                    scope.add(OidcScopes.EMAIL);
                    scope.add(OidcScopes.PROFILE);
                })
                .clientSettings(ClientSettings.builder().requireProofKey(false).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(1000L)).build())
                .build());
    }
}
