package io.hydrocarbon.moutai.param.response.moutai;

import lombok.Data;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Data
public class LoginResponse {
    private Long userId;

    private String userName;

    private String mobile;

    private Integer verifyStatus;

    private String idCode;

    private Integer idType;

    private String token;

    private Integer userTag;

    private String cookie;

    private String did;

    private String birthday;
}
