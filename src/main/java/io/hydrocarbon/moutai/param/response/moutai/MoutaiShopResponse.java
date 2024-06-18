package io.hydrocarbon.moutai.param.response.moutai;

import io.hydrocarbon.moutai.constant.Constants;
import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import io.hydrocarbon.moutai.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoutaiShopResponse {
    private Long id;

    private String shopId;

    private String name;

    private String address;

    private String fullAddress;

    private String tenantName;

    private List<String> tags;

    private LocalTime openEndTime;

    private LocalTime openStartTime;

    private String provinceName;

    private String cityName;

    private String districtName;

    public static MoutaiShopResponse fromEntity(MoutaiShop entity) {
        return MoutaiShopResponse.builder()
                .id(entity.getId())
                .shopId(entity.getShopId())
                .name(entity.getName())
                .address(entity.getAddress())
                .fullAddress(entity.getFullAddress())
                .tenantName(entity.getTenantName())
                .tags(StringUtil.split(entity.getTags(), Constants.String.COMMA))
                .openEndTime(entity.getOpenEndTime())
                .openStartTime(entity.getOpenStartTime())
                .provinceName(entity.getProvinceName())
                .cityName(entity.getCityName())
                .districtName(entity.getDistrictName())
                .build();
    }
}
