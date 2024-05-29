package io.hydrocarbon.moutai.param.moutai.response;

import io.hydrocarbon.moutai.entity.moutai.MoutaiItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoutaiItemResponse {
    private Long id;

    private String itemCode;

    private String title;

    private String content;

    private String picture;

    private String pictureV2;

    public static MoutaiItemResponse fromEntity(MoutaiItem entity) {
        return MoutaiItemResponse.builder()
                .id(entity.getId())
                .itemCode(entity.getItemCode())
                .title(entity.getTitle())
                .content(entity.getContent())
                .picture(entity.getPicture())
                .pictureV2(entity.getPictureV2())
                .build();
    }
}
