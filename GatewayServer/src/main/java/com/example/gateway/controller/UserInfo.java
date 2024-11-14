package com.example.gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
public class UserInfo {

    @GetMapping("/me")
    public Mono<UserDto> userinfo(@AuthenticationPrincipal OidcUser user) {

        if (user != null) {
            return Mono.just(new UserDto(
                    user.getIdToken().getSubject(),
                    user.getIdToken().getClaim("details"),
                    user.getIdToken().getClaimAsStringList("authorities").stream().collect(Collectors.joining(","))
            ));
        }
        return Mono.just(UserDto.ANONYMOUS);
    }

    static record UserDto(
            String username,
            String details,
            String roles
    ) {
        static final UserDto ANONYMOUS = new UserDto("ANONYMUS", "tutorial", "NONE");
    }
}
