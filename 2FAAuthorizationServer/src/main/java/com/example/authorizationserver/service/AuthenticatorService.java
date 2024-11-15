package com.example.authorizationserver.service;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

@Service
public class AuthenticatorService {

    public boolean check(String key, String code) {
        try {
            return TimeBasedOneTimePasswordUtil.validateCurrentNumber(key, Integer.parseInt(code), 10000);
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
        catch (GeneralSecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public String generateSecret() {
        return TimeBasedOneTimePasswordUtil.generateBase32Secret();
    }

    public String generateQrImageUrl(String keyId, String base32Secret) {
        //  return "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Example";
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + TimeBasedOneTimePasswordUtil.generateOtpAuthUrl(keyId, base32Secret);
//        return TimeBasedOneTimePasswordUtil.qrImageUrl(keyId, base32Secret);
    }

    public String getCode(String base32Secret) throws GeneralSecurityException {
        return TimeBasedOneTimePasswordUtil.generateCurrentNumberString(base32Secret);
    }
}
