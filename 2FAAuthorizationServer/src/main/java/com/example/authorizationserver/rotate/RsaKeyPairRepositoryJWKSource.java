package com.example.authorizationserver.rotate;

import com.example.authorizationserver.rotate.key.RsaKeyPair;
import com.example.authorizationserver.rotate.repository.RsaKeyPairRepository;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RsaKeyPairRepositoryJWKSource implements JWKSource<SecurityContext>, OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RsaKeyPairRepository rsaKeyPairRepository;

    public RsaKeyPairRepositoryJWKSource(RsaKeyPairRepository rsaKeyPairRepository) {
        this.rsaKeyPairRepository = rsaKeyPairRepository;
    }

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
        List<RsaKeyPair> keyPairs = rsaKeyPairRepository.findKeyPair();

        List<JWK> list = new ArrayList<>(keyPairs.size());
        for (RsaKeyPair k : keyPairs) {
            RSAKey build = new RSAKey.Builder(k.publicKey())
                    .privateKey(k.privateKey())
                    .keyID(k.id())
                    .build();

            if (jwkSelector.getMatcher().matches(build)) {
                // 설정에 해당하는 타입만
                list.add(build);
            }
        }
        return list;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication principal = context.getPrincipal();

        Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        // type
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            context.getClaims().claim("authorities", authorities);
        }

        // token.value
        if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
            context.getClaims()
                    .claim("authorities", authorities)
                    .claim("details", "yeonsoo made.");
        }

        // pass latest key
        List<RsaKeyPair> keyPairs = rsaKeyPairRepository.findKeyPair();
        String kid = keyPairs.get(0).id();
        JwsHeader jwsHeader = context.getJwsHeader().keyId(kid).build();

        System.out.println(jwsHeader);

    }

}
