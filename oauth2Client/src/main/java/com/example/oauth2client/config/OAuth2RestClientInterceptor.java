package com.example.oauth2client.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.io.IOException;

public class OAuth2RestClientInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final ClientRegistration clientRegistration;

    public OAuth2RestClientInterceptor(OAuth2AuthorizedClientManager authorizedClientManager, ClientRegistration clientRegistration) {
        this.authorizedClientManager = authorizedClientManager;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthorizedClient client = authorizedClientManager.authorize(
                OAuth2AuthorizeRequest
                        .withClientRegistrationId(clientRegistration.getRegistrationId())
                        .principal(authentication)
                        .build()
        );
        if (client == null) {
            throw new IllegalStateException("Could not authorize client " + clientRegistration.getRegistrationId());
        }
        request.getHeaders().set("Authorization", "Bearer " + client.getAccessToken().getTokenValue());

        // next
        return execution.execute(request, body);
    }
}
