package io.hydrocarbon.moutai.config;

import io.hydrocarbon.moutai.entity.auth.User;
import io.hydrocarbon.moutai.repository.auth.UserRepository;
import io.hydrocarbon.moutai.service.auth.CustomUserDetailsService;
import io.hydrocarbon.moutai.service.auth.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.ReservedClaimNames;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilterConfig extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    private final List<String> excludeUrls =
            List.of("/auth/login",
                    "/auth/register");

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     */
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (excludeUrls.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        // 不包含 Authorization 头的请求不进行认证
        if (!StringUtils.hasText(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 不包含 Bearer Token 的请求不进行认证
        if (!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.split(" ")[1];
        // 不包含 Token 的请求不进行认证
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, Object> claims = jwtService.getClaims(accessToken);
        String userId = claims.get(ReservedClaimNames.SUBJECT).toString();

        if (!StringUtils.hasText(userId)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        userOptional.ifPresentOrElse(user ->
                        SecurityContextHolder.getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(user,
                                        null,
                                        user.getAuthorities())),
                () -> log.error("User not found: {}", userId)
        );
        filterChain.doFilter(request, response);
    }
}
