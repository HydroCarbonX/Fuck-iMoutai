package io.hydrocarbon.moutai.param.request.auth;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
public record LoginBody(
        // 账号
        @NotEmpty
        String account,

        // 密码
        @NotEmpty
        String password
) {
}
