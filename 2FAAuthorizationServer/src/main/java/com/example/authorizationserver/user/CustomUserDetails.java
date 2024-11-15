package com.example.authorizationserver.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;

//@JsonDeserialize
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class CustomUserDetails implements UserDetails {
//    private final User user;
//
//    @JsonCreator
//    public CustomUserDetails(@JsonProperty("user") User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user.authorities();
//    }
//
//    @Override
//    @JsonIgnore
//    public String getPassword() {
//        return user.password();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.username();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return user.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return user.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return user.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return user.enabled();
//    }
//
//    public User getUser() {
//        return new User(
//                user.username(), "PROTECTED", user.enabled(), user.isAccountNonExpired(),
//                user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.authorities(),
//                user.securityQuestion(), user.answer(), user.mfaSecret(), user.mfaKeyId(),
//                user.mfaEnabled(), user.mfaRegistered(), user.securityQuestionEnabled());
//    }
//}
