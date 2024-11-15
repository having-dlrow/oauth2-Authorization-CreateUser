package com.example.authorizationserver.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        String username,
        String password,
        boolean enabled,
        boolean isAccountNonExpired,
        boolean isAccountNonLocked,
        boolean isCredentialsNonExpired,
        Collection<? extends GrantedAuthority> authorities,
        String securityQuestion,
        String answer,
        String mfaSecret,
        String mfaKeyId,
        boolean mfaEnabled,
        boolean mfaRegistered,
        boolean securityQuestionEnabled
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities();
    }

    @Override
    public String getPassword() {
        return password();
    }

    @Override
    public String getUsername() {
        return username();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
