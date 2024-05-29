package io.hydrocarbon.moutai.entity.moutai;

import io.hydrocarbon.moutai.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

/**
 * 茅台预约信息表
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_appointment")
public class MoutaiAppointment extends BaseEntity {
    /**
     * 用户 ID
     */
    @NotNull
    @Column(name = "moutai_user_id", nullable = false)
    private Long moutaiUserId;

    /**
     * 店 ID
     */
    @Size(max = 32)
    @NotNull
    @Column(name = "moutai_shop_shop_id", nullable = false, length = 32)
    private String moutaiShopShopId;

    /**
     * 编码
     */
    @Size(max = 64)
    @NotNull
    @Column(name = "moutai_item_item_code", nullable = false, length = 64)
    private String moutaiItemItemCode;

    /**
     * 关联店铺
     */
    @OneToOne(targetEntity = MoutaiShop.class)
    @JoinColumn(name = "moutai_shop_shop_id", referencedColumnName = "shop_id", insertable = false, updatable = false)
    @SQLRestriction("is_deleted = FALSE")
    private MoutaiShop moutaiShop;

    /**
     * 关联商品
     */
    @OneToOne(targetEntity = MoutaiItem.class)
    @JoinColumn(name = "moutai_item_item_code", referencedColumnName = "item_code", insertable = false, updatable = false)
    @SQLRestriction("is_deleted = FALSE")
    private MoutaiItem moutaiItem;
}
