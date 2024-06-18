package io.hydrocarbon.moutai.util;

import io.hydrocarbon.moutai.entity.auth.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * Context 工具类
 *
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ContextUtil {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }

        if (!authentication.isAuthenticated()) {
            return null;
        }

        if (authentication.getPrincipal() instanceof User user) {
            return user;
        }

        return null;
    }
}
