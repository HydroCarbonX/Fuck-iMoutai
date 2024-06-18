package io.hydrocarbon.moutai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class PasswordUtil {

    private static PasswordEncoder passwordEncoder;

    @Autowired
    @SuppressWarnings("java:S2696")
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        PasswordUtil.passwordEncoder = passwordEncoder;
    }

    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
