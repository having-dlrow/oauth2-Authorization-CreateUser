package com.example.oauth2client.config;

import com.example.oauth2client.service.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${spring.security.oauth2.client.registration.springOAuth2.client-id}")
    String client_id;

    @Value("${restclient-url}")
    String restclient_url;

    @Bean
    public UserClient userClient(
            OAuth2AuthorizedClientManager authorizedClientManager,
            ClientRegistrationRepository clientRegistrationRepository
    ) {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(client_id);

        RestClient restClient = RestClient.builder()
                .baseUrl(restclient_url)
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
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository
        ) {
        // manager
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        // manager.setProvider
        authorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .authorizationCode()
                        .refreshToken()
                        .build()
        );

        return authorizedClientManager;
    }
}
