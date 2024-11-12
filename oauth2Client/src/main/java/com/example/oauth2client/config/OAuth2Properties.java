package com.example.oauth2client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Properties {

    @Value("${restclient-url}")
    String restclient_url;

    @Value("${spring.security.oauth2.client.registration.youtube-lecture-oauth2.client-id}")
    private String clientId;

    public String getRestClientUrl() {
        return restclient_url;
    }

    public String getClientId() {
        return clientId;
    }

    public String getLoginUrl() {
        return "/oauth2/authorization/" + clientId;
    }

}
