package com.example.authorizationserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    /* ID Token, Access Token 모두 적용됨 */
    /* RsaKeyPairRepositoryJWKSource */
//    @Bean
//    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
//        return new OAuth2TokenCustomizer<JwtEncodingContext>() {
//            @Override
//            public void customize(JwtEncodingContext context) {
//                Set<String> authorities = context.getPrincipal()
//                        .getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toSet());
//
//                switch (context.getTokenType().getValue()) {
//                    case "id_token":
//                        context.getClaims()
//                                .claim("Test", "Test ID Token")
//                                // Access Token의 만료 시간을 7일로 설정
////                                .claim("exp", System.currentTimeMillis() / 1000 + 604800)
//                                .claim("authorities", authorities)
//                                .claim("user", context.getPrincipal().getName());
//                        break;
//                    case "access_token":
//                        context.getClaims()
//                                .claim("Test", "Test Access Token")
//                                // Access Token의 만료 시간을 7일로 설정
////                                .claim("exp", System.currentTimeMillis() / 1000 + 604800)
//                                .claim("authorities", authorities);
//                        break;
//                }
//
//            }
//        };
//    }

    @Bean
    TextEncryptor textEncryptor(@Value("${jwt.encryptor.password}") String password, @Value("${jwt.encryptor.salt}") String salt) {
        return Encryptors.text(password, salt);
    }

    @Bean
    OAuth2TokenGenerator<OAuth2Token> delegatingOAuth2TokenGenerator(JwtEncoder encoder,
                                                                     // RsaKeyPairRepositoryJWKSource
                                                                     OAuth2TokenCustomizer<JwtEncodingContext> customizer) {
        JwtGenerator generator = new JwtGenerator(encoder);
        generator.setJwtCustomizer(customizer);
        return new DelegatingOAuth2TokenGenerator(generator, new OAuth2AccessTokenGenerator(), new OAuth2RefreshTokenGenerator());
    }


}
