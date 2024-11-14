package com.example.authorizationserver.rotate.repository.serialize;

import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaPublicKeyConverter implements Serializer<RSAPublicKey>, Deserializer<RSAPublicKey> {

    private final TextEncryptor textEncryptor;

    public RsaPublicKeyConverter(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;
    }

    @Override
    public RSAPublicKey deserialize(InputStream inputStream) throws IOException {
        String pem = this.textEncryptor.decrypt(FileCopyUtils.copyToString(new InputStreamReader(inputStream)));
        String publicKeyPEM = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        try {
            byte[] encoded = Base64.getMimeDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = null;
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("There's been an exception", e);
        }
    }

    @Override
    public void serialize(RSAPublicKey key, OutputStream outputStream) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
        String string = "-----BEGIN PUBLIC KEY-----\n"
                + Base64.getMimeEncoder().encodeToString(x509EncodedKeySpec.getEncoded())
                + "\n-----END PUBLIC KEY-----";
        outputStream.write(this.textEncryptor.encrypt(string).getBytes());
    }


    @Component
    public class RsaPrivateKeyConverter implements Serializer<RSAPrivateKey>, Deserializer<RSAPrivateKey> {

        private final TextEncryptor textEncryptor;

        public RsaPrivateKeyConverter(TextEncryptor textEncryptor) {
            this.textEncryptor = textEncryptor;
        }

        @Override
        public RSAPrivateKey deserialize(InputStream inputStream) throws IOException {
            String pem = this.textEncryptor.decrypt(FileCopyUtils.copyToString(new InputStreamReader(inputStream)));
            String privateKeyPEM = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");

            try {
                byte[] encoded = Base64.getMimeDecoder().decode(privateKeyPEM);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

                return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("There's been an exception", e);
            }
        }

        @Override
        public void serialize(RSAPrivateKey key, OutputStream outputStream) throws IOException {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key.getEncoded());
            String string = "-----BEGIN PRIVATE KEY-----\n"
                    + Base64.getMimeEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded())
                    + "\n-----END PRIVATE KEY-----";
            outputStream.write(this.textEncryptor.encrypt(string).getBytes());
        }
    }

}