package com.example.authorizationserver.controller;

import com.example.authorizationserver.securityHandler.MFAAuthentication;
import com.example.authorizationserver.securityHandler.MFAHandler;
//import com.example.authorizationserver.service.AuthenticationStore;
import com.example.authorizationserver.service.AuthenticatorService;
import com.example.authorizationserver.user.CustomUserDetailsService;
import com.example.authorizationserver.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.security.GeneralSecurityException;

@Controller
public class LoginController {

    /* consent */
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    /* qrcode */
    private final AuthenticatorService authenticatorService;

    private final CustomUserDetailsService customUserDetailsService;

    /* 2fa */
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
//    private final AuthenticationStore authenticationStore;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationSuccessHandler securityQuestionSuccessHandler =
            new MFAHandler("/security-question", "ROLE_SECURITY_QUESTION_REQUIRED");

    private final AuthenticationFailureHandler authenticatorFailureHandler = new SimpleUrlAuthenticationFailureHandler("/authenticator?error");
    private final AuthenticationFailureHandler securityQuestionFailureHandler = new SimpleUrlAuthenticationFailureHandler("/security-question?error");

    /* qrcode */
    private String generatedCode = "";
    private String base32Secret = "";
    private String keyId = "";

    public LoginController(
            OAuth2AuthorizationConsentService authorizationConsentService
            , AuthenticatorService authenticatorService
            , CustomUserDetailsService customUserDetailsService
            , AuthenticationSuccessHandler authenticationSuccessHandler
//            , AuthenticationStore authenticationStore
    ) {

        this.authorizationConsentService = authorizationConsentService;
        this.authenticatorService = authenticatorService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
//        this.authenticationStore = authenticationStore;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(
            Model model,
            @CurrentSecurityContext SecurityContext context) {
        base32Secret = authenticatorService.generateSecret();
        keyId = getUser(context).mfaKeyId();
        try {
            generatedCode = authenticatorService.getCode(base32Secret);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        System.err.println(generatedCode);
        String qrImage = authenticatorService.generateQrImageUrl(keyId, base32Secret);
        model.addAttribute("qrImage", qrImage);
        return "registration";
    }

    @PostMapping("/registration")
    public void validateRegistration(@RequestParam("code") String code,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     @CurrentSecurityContext SecurityContext context) throws ServletException, IOException {
        if (code.equals(generatedCode)) {
            customUserDetailsService.saveUserInfoMfaRegistered(base32Secret, getUser(context).username());
            if (!getUser(context).securityQuestionEnabled()) {
                this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthentication(request, response));
                return;
            }
            this.securityQuestionSuccessHandler.onAuthenticationSuccess(request, response, getAuthentication(request, response));
            return;
        }
        this.authenticatorFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad credentials"));
    }

    @GetMapping(value = "/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.STATE) String state) {

        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();
        OAuth2AuthorizationConsent previousConsent = this.authorizationConsentService.findById(clientId, principal.getName());
        for (String scopeFromRequest : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (previousConsent != null && previousConsent.getScopes().contains(scopeFromRequest)) {
                previouslyApprovedScopes.add(scopeFromRequest);
            } else {
                scopesToApprove.add(scopeFromRequest);
            }
        }

        model.addAttribute("state", state);
        model.addAttribute("clientId", clientId);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());

        return "consent";
    }

    private Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        return scopes
                .stream()
                .map(ScopeWithDescription::new)
                .collect(Collectors.toSet());
    }

    static class ScopeWithDescription {

        public final String scope;
        public final String description;

        private final static String DEFAULT_DESCRIPTION = "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";
        private static final Map<String, String> scopeDescriptions = new HashMap<>();

        static {
            scopeDescriptions.put(
                    "openid",
                    "use openidc to verify your identity"
            );
            scopeDescriptions.put(
                    "profile",
                    "profile information for personalization"
            );
        }

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = scopeDescriptions.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }
    }

    @GetMapping("/authenticator")
    public String authenticator(
        @CurrentSecurityContext SecurityContext context) {
        if (!getUser(context).mfaRegistered()) {
            return "redirect:registration";
        }
        return "authenticator";
    }


    @PostMapping("/authenticator")
    public void validateCode(
            @RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response,
            @CurrentSecurityContext SecurityContext context) throws ServletException, IOException {

//         if(code.equals(getUser(context).mfaSecret())) {
        if (authenticatorService.check(getUser(context).mfaSecret(), code)) {
            if (!getUser(context).securityQuestionEnabled()) {
                this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthentication(request, response));
                return;
            }
            this.securityQuestionSuccessHandler.onAuthenticationSuccess(request, response, getAuthentication(request, response));
            return;
        }
        authenticatorFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad credentials"));

    }

//    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = authenticationStore.getAuthentication();
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        securityContextRepository.saveContext(securityContext, request, response);
//        return authentication;
//    }

    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // anonymous -> mfa
        MFAAuthentication mfaAuthentication = (MFAAuthentication) securityContext.getAuthentication();

        securityContext.setAuthentication(mfaAuthentication.getAuthentication());
        SecurityContextHolder.setContext(securityContext);

        securityContextRepository.saveContext(securityContext, request, response);
        return mfaAuthentication.getAuthentication();
    }

    @GetMapping("/security-question")
    public String securityQuestion(Model model,@CurrentSecurityContext SecurityContext context) {
        model.addAttribute("question", getUser(context).securityQuestion());
        return "security-question";
    }

    @PostMapping("/security-question")
    public void validateSecurityQuestion(
            @RequestParam("answer") String answer,
            HttpServletRequest request,
            HttpServletResponse response,
            @CurrentSecurityContext SecurityContext context) throws ServletException, IOException {
        if (answer.equals(getUser(context).answer())) {
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthentication(request, response));
            return;
        }
        this.securityQuestionFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad credentials"));
    }

    private User getUser(SecurityContext context) {
        // context > Authentication > Principal > CustomUserDetails
        MFAAuthentication mfaAuthentication = (MFAAuthentication) context.getAuthentication();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) mfaAuthentication.getAuthentication();
        User user = (User) usernamePasswordAuthenticationToken.getPrincipal();
        return user;
    }
}
