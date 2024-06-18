package io.hydrocarbon.moutai.service.auth;

import io.hydrocarbon.moutai.entity.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@Service
@Slf4j
public class JwtService {

    private final RsaJsonWebKey rsaJsonWebKey;

    public JwtService(RSAPrivateKey privateKey,
                      RSAPublicKey publicKey) {
        rsaJsonWebKey = new RsaJsonWebKey(publicKey);
        rsaJsonWebKey.setPrivateKey(privateKey);
        rsaJsonWebKey.setKeyId("k1");
    }

    /**
     * 生成 JWT
     *
     * @param user 用户信息
     * @return JWT
     */
    public String generateToken(User user) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setIssuer(String.valueOf(user.getId()));
        jwtClaims.setExpirationTimeMinutesInTheFuture(30);
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setSubject(String.valueOf(user.getId()));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());

        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            log.error("Generate JWT error", e);
            return null;
        }

        return jwt;
    }

    public Map<String, Object> getClaims(String token) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();

        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getClaimsMap();
        } catch (InvalidJwtException e) {
            log.error("Invalid JWT", e);
        }
        return Collections.emptyMap();
    }
}
