package io.hydrocarbon.moutai.entity.moutai;

import io.hydrocarbon.moutai.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

/**
 * 茅台用户信息
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_user")
public class MoutaiUser extends BaseEntity {

    /**
     * 设备 ID
     */
    @Size(max = 36)
    @NotNull
    @Column(name = "device_id", nullable = false, length = 36)
    private String deviceId;

    /**
     * 用户 ID
     */
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 用户名
     */
    @Size(max = 64)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 64)
    private String userName;

    /**
     * 手机号
     */
    @Size(max = 16)
    @NotNull
    @Column(name = "mobile", nullable = false, length = 16)
    private String mobile;

    /**
     * 认证状态
     */
    @NotNull
    @Column(name = "verify_status", nullable = false)
    private Integer verifyStatus;

    /**
     * 身份证号
     */
    @Size(max = 32)
    @NotNull
    @Column(name = "id_code", nullable = false, length = 32)
    private String idCode;

    /**
     * 证件类型
     */
    @NotNull
    @Column(name = "id_type", nullable = false)
    private Integer idType;

    /**
     * Token
     */
    @Size(max = 1024)
    @NotNull
    @Column(name = "token", nullable = false, length = 1024)
    private String token;

    /**
     * 用户标签
     */
    @Column(name = "user_tag")
    private Integer userTag;

    /**
     * Cookie
     */
    @Size(max = 1024)
    @NotNull
    @Column(name = "cookie", nullable = false, length = 1024)
    private String cookie;

    /**
     * 设备 ID
     */
    @Size(max = 1024)
    @NotNull
    @Column(name = "did", nullable = false, length = 1024)
    private String did;

    /**
     * 生日
     */
    @Size(max = 32)
    @Column(name = "birthday", length = 32)
    private String birthday;

    // 关联 moutai_appointment 表
    @OneToMany(targetEntity = MoutaiAppointment.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "moutai_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @SQLRestriction("is_deleted = FALSE")
    private List<MoutaiAppointment> moutaiAppointmentList;
}
