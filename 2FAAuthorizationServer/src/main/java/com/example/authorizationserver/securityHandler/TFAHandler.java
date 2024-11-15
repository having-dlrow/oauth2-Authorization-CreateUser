//package com.example.authorizationserver.securityHandler;
//
//import com.example.authorizationserver.service.AuthenticationStore;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//
//import java.io.IOException;
//
///*
// ```
// .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .successHandler(new TFAHandler(authenticationStore))
// ```
//*/
//
//public class TFAHandler implements AuthenticationSuccessHandler {
//
//    private static final Authentication ANONYMOUS_AUTHENTICATION
//            = new AnonymousAuthenticationToken(
//            "anonymous",
//            "anonymousUser",
//            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS", "ROLE_2FA_REQUIRED"));
//
//    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
//
//    private final AuthenticationSuccessHandler authenticationSuccessHandler;
//    private final AuthenticationStore authenticationStore;
//
//    public TFAHandler(AuthenticationStore authenticationStore) {
//
//        SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler("/authenticator");
//        authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
//
//        this.authenticationSuccessHandler = authenticationSuccessHandler;
//        this.authenticationStore = authenticationStore;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        /* 인증된 Authentication, session 객체에 저장 */
//        authenticationStore.saveAuthentication(authentication);
//        setAnonymousAuthentication(request, response);
//        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, ANONYMOUS_AUTHENTICATION);
//    }
//
//    private void setAnonymousAuthentication(HttpServletRequest request, HttpServletResponse response) {
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(ANONYMOUS_AUTHENTICATION); // anonymous
//        SecurityContextHolder.setContext(securityContext);
//        securityContextRepository.saveContext(securityContext, request, response);
//    }
//}
