package io.hydrocarbon.moutai.param.request.moutai;

import lombok.Data;

import java.util.List;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
public class MoutaiUserModifyRequest {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 设备 ID
     */
    private String deviceId;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 预约信息列表
     */
    private List<MoutaiUserAddRequest.AppointmentRequest> appointmentInfos;
}
