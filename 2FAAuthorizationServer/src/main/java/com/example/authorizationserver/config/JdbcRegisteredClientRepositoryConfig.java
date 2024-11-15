package com.example.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Configuration
public class JdbcRegisteredClientRepositoryConfig {
    @Bean
    RegisteredClientRepository jdbcRegisteredClientRepository(JdbcTemplate template) {

//        return RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .clientName(clientId)
//                .clientIdIssuedAt(Instant.now())
//                .clientSecretExpiresAt(Instant.MAX)
//                .clientAuthenticationMethods(methods -> {
//                    methods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
//                })
//                .authorizationGrantTypes(types -> {
//                    types.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//                    types.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//                    types.add(AuthorizationGrantType.REFRESH_TOKEN);
//                })
//                .redirectUri("http://127.0.0.1:8081")
//                .redirectUri("http://127.0.0.1:8081/login/oauth2/code/springoauth2")
//                .postLogoutRedirectUri("http://127.0.0.1/8081/logged-out")
//                .scopes(scope -> {
//                    scope.add(OidcScopes.OPENID);
//                    scope.add(OidcScopes.EMAIL);
//                    scope.add(OidcScopes.PROFILE);
//                    scope.add(scope1);
//                    scope.add(scope2);
//                    scope.add("photo.write");
//                    scope.add("photo.read");
//                })
//                .clientSettings(ClientSettings.builder().requireProofKey(false).build())  // pkce
////                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(1000L)).build())
//                .build();

        return new JdbcRegisteredClientRepository(template);
    }
}
