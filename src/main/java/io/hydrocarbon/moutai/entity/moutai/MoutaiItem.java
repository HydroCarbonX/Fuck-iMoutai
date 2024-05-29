package io.hydrocarbon.moutai.entity.moutai;

import io.hydrocarbon.moutai.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 茅台商品
 */
@Getter
@Setter
@Entity
@Table(name = "moutai_item")
public class MoutaiItem extends BaseEntity {
    /**
     * 编码
     */
    @Size(max = 64)
    @NotNull
    @Column(name = "item_code", nullable = false, length = 64)
    private String itemCode;

    /**
     * 标题
     */
    @Size(max = 128)
    @Column(name = "title", length = 128)
    private String title;

    /**
     * 内容
     */
    @Size(max = 512)
    @Column(name = "content", length = 512)
    private String content;

    /**
     * 图片
     */
    @Size(max = 1024)
    @Column(name = "picture", length = 1024)
    private String picture;

    /**
     * 图片
     */
    @Size(max = 1024)
    @Column(name = "picture_v2", length = 1024)
    private String pictureV2;

    /**
     * 跳转链接
     */
    @Size(max = 1024)
    @Column(name = "jump_url", length = 1024)
    private String jumpUrl;

    /**
     * 未知属性
     */
    @Column(name = "check_tag", columnDefinition = "int UNSIGNED")
    private Long checkTag;

    /**
     * 数量
     */
    @Column(name = "count", columnDefinition = "int UNSIGNED")
    private Long count;
}
