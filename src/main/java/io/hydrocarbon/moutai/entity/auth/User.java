package io.hydrocarbon.moutai.entity.auth;

import io.hydrocarbon.moutai.entity.BaseEntity;
import io.hydrocarbon.moutai.util.PasswordUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户表
 */
@Getter
@Setter
@Entity
@Table(name = "user")
@SQLRestriction("is_deleted = FALSE")
public class User extends BaseEntity implements UserDetails {

    /**
     * 用户名
     */
    @Size(max = 64)
    @NotNull
    @Column(name = "username", nullable = false, length = 64)
    private String username;

    /**
     * 密码（使用 BCrypt 加密）
     */
    @Size(max = 128)
    @NotNull
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    /**
     * 邮箱
     */
    @Size(max = 128)
    @NotNull
    @Column(name = "email", nullable = false, length = 128)
    private String email;

    /**
     * 手机号
     */
    @Size(max = 32)
    @NotNull
    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    /**
     * 头像
     */
    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    /**
     * 昵称
     */
    @Size(max = 64)
    @Column(name = "nickname", length = 64)
    private String nickname;

    /**
     * 是否过期
     */
    @NotNull
    @ColumnDefault("FALSE")
    @Column(name = "expired", nullable = false)
    private Boolean expired = false;

    /**
     * 是否锁定
     */
    @NotNull
    @ColumnDefault("FALSE")
    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    /**
     * 是否凭证过期
     */
    @NotNull
    @ColumnDefault("FALSE")
    @Column(name = "credentials_expired", nullable = false)
    private Boolean credentialsExpired = false;

    /**
     * 是否启用
     */
    @NotNull
    @ColumnDefault("TRUE")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    public void setPassword(String password) {
        this.password = PasswordUtil.encode(password);
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return expired;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
