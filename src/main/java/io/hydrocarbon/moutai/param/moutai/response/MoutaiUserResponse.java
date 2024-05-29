package io.hydrocarbon.moutai.param.moutai.response;

import io.hydrocarbon.moutai.constant.Constants;
import io.hydrocarbon.moutai.entity.moutai.MoutaiUser;
import io.hydrocarbon.moutai.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoutaiUserResponse {
    private Long id;

    private String mobile;

    private String deviceId;

    private List<MoutaiAppointmentResponse> appointmentList;

    public static MoutaiUserResponse fromEntity(MoutaiUser moutaiUser) {
        if (Objects.isNull(moutaiUser)) {
            return null;
        }
        return MoutaiUserResponse.builder()
                .id(moutaiUser.getId())
                .mobile(moutaiUser.getMobile())
                .deviceId(moutaiUser.getDeviceId())
                .appointmentList(
                        moutaiUser.getMoutaiAppointmentList().stream()
                                .filter(Objects::nonNull)
                                .map(moutaiAppointment -> MoutaiAppointmentResponse.builder()
                                        .shopId(moutaiAppointment.getMoutaiShop().getId())
                                        .shopName(moutaiAppointment.getMoutaiShop().getName())
                                        .fullAddress(moutaiAppointment.getMoutaiShop().getFullAddress())
                                        .tagList(
                                                StringUtil.split(moutaiAppointment.getMoutaiShop().getTags(),
                                                        Constants.String.COMMA))
                                        .itemId(moutaiAppointment.getMoutaiItem().getId())
                                        .itemName(moutaiAppointment.getMoutaiItem().getTitle())
                                        .content(moutaiAppointment.getMoutaiItem().getContent())
                                        .picture(moutaiAppointment.getMoutaiItem().getPicture())
                                        .pictureV2(moutaiAppointment.getMoutaiItem().getPictureV2())
                                        .build())
                                .toList()
                )
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoutaiAppointmentResponse {

        /**
         * 店铺 ID
         */
        private Long shopId;

        /**
         * 店铺名称
         */
        private String shopName;

        /**
         * 店铺地址
         */
        private String fullAddress;

        /**
         * tag
         */
        private List<String> tagList;

        /**
         * 商品 ID
         */
        private Long itemId;

        /**
         * 商品名称
         */
        private String itemName;

        /**
         * 商品内容介绍
         */
        private String content;

        /**
         * 商品图片
         */
        private String picture;

        /**
         * 商品图片 V2
         */
        private String pictureV2;
    }
}
