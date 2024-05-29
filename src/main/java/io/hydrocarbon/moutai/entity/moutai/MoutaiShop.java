package io.hydrocarbon.moutai.entity.moutai;

import io.hydrocarbon.moutai.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 茅台门店信息
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_shop")
public class MoutaiShop extends BaseEntity {
    /**
     * 店 ID
     */
    @Size(max = 32)
    @NotNull
    @Column(name = "shop_id", nullable = false, length = 32, unique = true)
    private String shopId;

    /**
     * 店名
     */
    @Size(max = 128)
    @Column(name = "name", length = 128)
    private String name;

    /**
     * 地址
     */
    @Size(max = 256)
    @Column(name = "address", length = 256)
    private String address;

    /**
     * 完整地址
     */
    @Size(max = 256)
    @Column(name = "full_address", length = 256)
    private String fullAddress;

    /**
     * 公司名
     */
    @Size(max = 256)
    @Column(name = "tenant_name", length = 256)
    private String tenantName;

    /**
     * 标签
     */
    @Size(max = 256)
    @Column(name = "tags", length = 256)
    private String tags;

    /**
     * 关门时间
     */
    @Column(name = "open_end_time")
    private LocalTime openEndTime;

    /**
     * 开门时间
     */
    @Column(name = "open_start_time")
    private LocalTime openStartTime;

    /**
     * 省份 Code
     */
    @Column(name = "province", columnDefinition = "int UNSIGNED")
    private Long province;

    /**
     * 省份名
     */
    @Size(max = 32)
    @Column(name = "province_name", length = 32)
    private String provinceName;

    /**
     * 城市 Code
     */
    @Column(name = "city", columnDefinition = "int UNSIGNED")
    private Long city;

    /**
     * 城市名
     */
    @Size(max = 64)
    @Column(name = "city_name", length = 64)
    private String cityName;

    /**
     * 区县 Code
     */
    @Column(name = "district", columnDefinition = "int UNSIGNED")
    private Long district;

    /**
     * 区县名
     */
    @Size(max = 64)
    @Column(name = "district_name", length = 64)
    private String districtName;

    /**
     * 经度
     */
    @Column(name = "lng", precision = 16, scale = 6)
    private BigDecimal lng;

    /**
     * 维度
     */
    @Column(name = "lat", precision = 16, scale = 6)
    private BigDecimal lat;

    /**
     * 是否支持分期付款
     */
    @Column(name = "layaway")
    private Boolean layaway;
}
