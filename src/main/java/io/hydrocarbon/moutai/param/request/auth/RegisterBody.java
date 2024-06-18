package io.hydrocarbon.moutai.param.request.auth;

import io.hydrocarbon.moutai.entity.auth.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
public record RegisterBody(
        // 用户名
        @Size(max = 64)
        @NotNull
        String username,

        // 密码
        @Size(max = 128)
        @NotNull
        String password,

        // 邮箱
        @Size(max = 128)
        @NotNull
        String email,

        // 手机号
        @Size(max = 32)
        @NotNull
        String phone,

        // 头像
        @Size(max = 255)
        String avatar,

        // 昵称
        @Size(max = 64)
        String nickname
) {

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAvatar(avatar);
        user.setNickname(nickname);
        user.setEnabled(true);
        return user;
    }
}
