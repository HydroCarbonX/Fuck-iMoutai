package io.hydrocarbon.moutai.entity.moutai;

import io.hydrocarbon.moutai.entity.BaseEntity;
import io.hydrocarbon.moutai.enums.MoutaiReserveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * 茅台预约记录表
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_appointment_record")
public class MoutaiAppointmentRecord extends BaseEntity {
    /**
     * 用户 ID
     */
    @NotNull
    @Column(name = "moutai_user_id", nullable = false)
    private Long moutaiUserId;

    /**
     * 预约时间
     */
    @NotNull
    @Column(name = "reserve_time", nullable = false)
    private OffsetDateTime reserveTime;

    /**
     * 预约状态
     */
    @Enumerated
    @Column(name = "reserve_status", columnDefinition = "int UNSIGNED not null")
    private MoutaiReserveStatus reserveStatus;
}
