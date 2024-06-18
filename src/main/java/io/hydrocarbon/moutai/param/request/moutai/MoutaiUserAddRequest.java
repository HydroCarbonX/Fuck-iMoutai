package io.hydrocarbon.moutai.param.request.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiUser;
import io.hydrocarbon.moutai.param.response.moutai.LoginResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
public class MoutaiUserAddRequest {

    /**
     * 手机号
     */
    @NotNull
    private String mobile;

    /**
     * 设备 ID
     */
    @NotNull
    private String deviceId;

    /**
     * 验证码
     */
    @NotNull
    private String verificationCode;

    /**
     * 预约信息列表
     */
    private List<AppointmentRequest> appointmentInfos;

    /**
     * 转换为实体
     *
     * @param loginResponse 登录响应数据
     * @return 实体
     */
    public MoutaiUser toEntity(LoginResponse loginResponse) {
        if (Objects.isNull(loginResponse)) {
            return null;
        }

        MoutaiUser moutaiUser = new MoutaiUser();
        moutaiUser.setDeviceId(getDeviceId());
        moutaiUser.setUserId(loginResponse.getUserId());
        moutaiUser.setUserName(loginResponse.getUserName());
        moutaiUser.setMobile(getMobile());
        //noinspection DuplicatedCode
        moutaiUser.setVerifyStatus(loginResponse.getVerifyStatus());
        moutaiUser.setIdCode(loginResponse.getIdCode());
        moutaiUser.setIdType(loginResponse.getIdType());
        moutaiUser.setToken(loginResponse.getToken());
        moutaiUser.setUserTag(loginResponse.getUserTag());
        moutaiUser.setCookie(loginResponse.getCookie());
        moutaiUser.setDid(loginResponse.getDid());
        moutaiUser.setBirthday(loginResponse.getBirthday());
        return moutaiUser;
    }

    @Data
    public static class AppointmentRequest {

        /**
         * 店铺 ID
         */
        private Long shopId;

        /**
         * 商品编码
         */
        private Long itemId;
    }
}
