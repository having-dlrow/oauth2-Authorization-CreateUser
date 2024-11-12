package com.example.oauth2client.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
public class OAuth2RestClientInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final ClientRegistration clientRegistration;

    /* restClient add Header */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // Authentication 에서 인증완료된 Oauth2Client 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistration.getRegistrationId())
                .principal(authentication)
                .build();
        OAuth2AuthorizedClient client = authorizedClientManager.authorize(authorizeRequest);
        if (client == null) {
            throw new IllegalStateException("Could not authorize client " + clientRegistration.getRegistrationId());
        }

        log.info("userClient add Header: Authorization Bearer " + client.getAccessToken().getTokenValue());
        request.getHeaders().set("Authorization", "Bearer " + client.getAccessToken().getTokenValue());

        // next
        return execution.execute(request, body);
    }
}
