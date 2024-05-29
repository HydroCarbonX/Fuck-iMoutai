package io.hydrocarbon.moutai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@MappedSuperclass
@Getter
@Setter
@SQLRestriction("is_deleted = FALSE")
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED", nullable = false, updatable = false)
    private Long id;

    @CreationTimestamp
    @Column(columnDefinition = "DATETIME", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(columnDefinition = "DATETIME", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "BIGINT UNSIGNED", nullable = false, updatable = false)
    private Long createdBy = 0L;

    @Column(columnDefinition = "BIGINT UNSIGNED", nullable = false)
    private Long updatedBy = 0L;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private Boolean isDeleted = false;

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Class<?> oEffectiveClass = obj instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : obj.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BaseEntity that = (BaseEntity) obj;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
