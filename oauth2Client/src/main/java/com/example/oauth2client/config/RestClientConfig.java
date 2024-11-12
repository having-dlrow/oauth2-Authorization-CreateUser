package com.example.oauth2client.config;

import com.example.oauth2client.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final OAuth2Properties oAuth2Properties;

    @Bean
    public UserClient userClient(
            OAuth2AuthorizedClientManager authorizedClientManager,
            ClientRegistrationRepository clientRegistrationRepository
    ) {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(oAuth2Properties.getClientId());

        RestClient restClient = RestClient.builder()
                .baseUrl(oAuth2Properties.getRestClientUrl())
                .requestInterceptor(
                        new OAuth2RestClientInterceptor(authorizedClientManager, clientRegistration)
                )
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(restClient)
        ).build();
        return factory.createClient(UserClient.class);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            InMemoryClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository
        ) {
        // manager
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        // manager.setProvider
        authorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
        //                .password() // deprecated
                        .clientCredentials()
                        .refreshToken()
                        .build()
        );

        return authorizedClientManager;
    }
}
