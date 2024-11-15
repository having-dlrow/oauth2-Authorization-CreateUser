//package com.example.authorizationserver.user;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Deprecated
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private UserDetailsService userDetailsService;
//
//    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) {
//
//        String username = (String) authentication.getPrincipal();
//        String password = (String) authentication.getCredentials();
//
//        User user = (User) userDetailsService.loadUserByUsername(username);
//
//        if (!matchPassword(password, user.getPassword())) {
//            throw new BadCredentialsException("비밀번호 안맞음!!!!!");
//        }
//
//        if (!user.isEnabled()) {
//            throw new BadCredentialsException("계정활성화 안되있음!!!!!");
//        }
//
//        return new UsernamePasswordAuthenticationToken(user, password, user.());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//
//    private boolean matchPassword(String loginPwd, String password) {
//        return loginPwd.equals(password);
//    }
//
//}
