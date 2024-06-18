package io.hydrocarbon.moutai.service.user;

import io.hydrocarbon.moutai.entity.auth.User;
import io.hydrocarbon.moutai.repository.auth.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public boolean userExists(User user) {
        Specification<User> userSpecification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.disjunction();

            if (Objects.nonNull(user.getUsername())) {
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("username"),
                        user.getUsername()));
            }

            if (Objects.nonNull(user.getEmail())) {
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("email"),
                        user.getEmail()));
            }

            if (Objects.nonNull(user.getPhone())) {
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("phone"),
                        user.getPhone()));
            }

            return predicate;
        };

        return !userRepository.findAll(userSpecification).isEmpty();
    }

    public Optional<User> findByAccount(String account) {
        Specification<User> userSpecification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.disjunction();
            predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("username"), account));
            predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("email"), account));
            predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("phone"), account));
            return predicate;
        };

        return userRepository.findOne(userSpecification);
    }

    public void disable(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }
}
