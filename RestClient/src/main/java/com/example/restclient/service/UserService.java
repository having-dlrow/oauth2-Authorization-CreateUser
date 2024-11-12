package com.example.restclient.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UserService {

    private final RestClient restClient;

    public UserService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestFactory(new JdkClientHttpRequestFactory())
                .build();
    }

    public List<User> findAllUsers() {
        return restClient.get()
                .uri("/user")
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});
    }

    public User findUserByUsername(String username) {
        return restClient.get()
                .uri("/user/{username}", username)
                .retrieve()
                .body(User.class);
    }

    public void createUser(User user) {
        restClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public void updateUser(User user) {
        restClient.put()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteUser(String username) {
        restClient.delete()
                .uri("/user/{username}", username)
                .retrieve()
                .toBodilessEntity();
    }
}
