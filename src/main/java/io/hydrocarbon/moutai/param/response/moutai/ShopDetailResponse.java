package io.hydrocarbon.moutai.param.response.moutai;

import io.hydrocarbon.moutai.constant.Constants;
import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import io.hydrocarbon.moutai.util.TimeUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@Data
public class ShopDetailResponse {
    private String address;

    private Long city;

    private String cityName;

    private Long district;

    private String districtName;

    private String fullAddress;

    private BigDecimal lat;

    private Boolean layaway;

    private BigDecimal lng;

    private String name;

    private String openEndTime;

    private String openStartTime;

    private Long province;

    private String provinceName;

    private String shopId;

    private List<String> tags;

    private String tenantName;

    public MoutaiShop toEntity() {
        MoutaiShop moutaiShop = new MoutaiShop();
        moutaiShop.setShopId(this.shopId);
        moutaiShop.setName(this.name);
        moutaiShop.setAddress(this.address);
        moutaiShop.setFullAddress(this.fullAddress);
        moutaiShop.setTenantName(this.tenantName);
        moutaiShop.setTags(getTagsString());
        moutaiShop.setOpenEndTime(TimeUtil.toLocalTime(this.openEndTime));
        moutaiShop.setOpenStartTime(TimeUtil.toLocalTime(this.openStartTime));
        moutaiShop.setProvince(this.province);
        moutaiShop.setProvinceName(this.provinceName);
        moutaiShop.setCity(this.city);
        moutaiShop.setCityName(this.cityName);
        moutaiShop.setDistrict(this.district);
        moutaiShop.setDistrictName(this.districtName);
        moutaiShop.setLng(this.lng);
        moutaiShop.setLat(this.lat);
        moutaiShop.setLayaway(this.layaway);
        return moutaiShop;
    }

    private String getTagsString() {
        if (tags == null || tags.isEmpty()) {
            return Constants.String.EMPTY;
        }

        return String.join(Constants.String.COMMA, tags);
    }
}
