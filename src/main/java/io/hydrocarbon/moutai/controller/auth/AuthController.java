package io.hydrocarbon.moutai.controller.auth;

import io.hydrocarbon.moutai.entity.auth.User;
import io.hydrocarbon.moutai.param.Response;
import io.hydrocarbon.moutai.param.request.auth.LoginBody;
import io.hydrocarbon.moutai.param.request.auth.RegisterBody;
import io.hydrocarbon.moutai.service.auth.AuthService;
import io.hydrocarbon.moutai.service.auth.JwtService;
import io.hydrocarbon.moutai.service.user.UserService;
import io.hydrocarbon.moutai.util.ContextUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-12
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final JwtService jwtService;

    /**
     * 平台用户注册
     *
     * @param body 注册信息
     * @return 注册结果：成功返回 accessToken，失败返回错误信息
     */
    @PostMapping("/register")
    public Response<String> register(@RequestBody @Valid
                                     RegisterBody body) {
        log.info("Register request: {}", body);

        if (userService.userExists(body.toUser())) {
            return Response.fail("用户已存在");
        }

        User registeredUser = authService.register(body);

        String accessToken = jwtService.generateToken(registeredUser);
        return Response.success(accessToken);
    }

    /**
     * 平台用户登录
     *
     * @param body 登录信息
     * @return 登录结果：成功返回 accessToken，失败返回错误信息
     */
    @PostMapping("/login")
    public Response<String> login(@RequestBody @Valid
                                  LoginBody body) {

        User currentUser = ContextUtil.getCurrentUser();
        log.info("Current user: {}", currentUser);

        User matchedUser = authService.authenticate(body);
        if (Objects.isNull(matchedUser)) {
            return Response.fail("用户名或密码错误");
        }

        String accessToken = jwtService.generateToken(matchedUser);
        return Response.success(accessToken);
    }

    @DeleteMapping("/{userId}")
    public Response<Void> disable(@PathVariable Long userId) {
        userService.disable(userId);
        return Response.success();
    }
}
