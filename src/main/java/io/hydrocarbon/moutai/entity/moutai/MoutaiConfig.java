package io.hydrocarbon.moutai.entity.moutai;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 茅台配置表
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_config")
public class MoutaiConfig {
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 键
     */
    @Size(max = 64)
    @NotNull
    @Column(name = "`key`", nullable = false, length = 64)
    private String key;

    /**
     * 值
     */
    @Size(max = 1024)
    @NotNull
    @Column(name = "value", nullable = false, length = 1024)
    private String value;
}
