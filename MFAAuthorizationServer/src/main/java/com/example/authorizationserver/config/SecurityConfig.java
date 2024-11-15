package com.example.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationEndpoint(auth -> auth
                        // @see LoginController
                        .consentPage("/oauth2/consent"))
                .oidc(Customizer.withDefaults());

        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/error", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                // .formLogin(Customizer.withDefaults());
                // @see LoginController
                .formLogin(formLogin -> formLogin.loginPage("/login"));

        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true)
                .ignoring()
                .requestMatchers("/webjars/**", "/images/**", "/css/**", "/assets/**", "/favicon.ico");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

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

    @Bean
    UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    OAuth2AuthorizationService jdbcOAuth2AuthorizationService(
            JdbcOperations jdbcOperations,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }

    @Bean
    OAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(
            JdbcOperations jdbcOperations,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
    }
}

