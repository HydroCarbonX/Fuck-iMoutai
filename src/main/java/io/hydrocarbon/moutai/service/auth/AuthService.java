package io.hydrocarbon.moutai.service.auth;

import io.hydrocarbon.moutai.entity.auth.User;
import io.hydrocarbon.moutai.param.request.auth.LoginBody;
import io.hydrocarbon.moutai.param.request.auth.RegisterBody;
import io.hydrocarbon.moutai.repository.auth.UserRepository;
import io.hydrocarbon.moutai.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public User register(RegisterBody body) {
        User user = body.toUser();
        userRepository.save(user);
        return user;
    }

    public User authenticate(LoginBody body) {
        String account = body.account();

        Optional<User> userOptional = userService.findByAccount(account);

        if (userOptional.isEmpty()) {
            log.info("User {} not found", account);
            return null;
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            return null;
        }

        return user;
    }
}
