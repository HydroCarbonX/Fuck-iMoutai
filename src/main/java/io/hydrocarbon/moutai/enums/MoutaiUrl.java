package io.hydrocarbon.moutai.enums;

import io.hydrocarbon.moutai.constant.Constants;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
public enum MoutaiUrl {

    /**
     * iMoutai resource url
     */
    RESOURCE_URL("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/resource/get", HttpMethod.GET),

    /**
     * iMoutai item url
     */
    ITEM_URL("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/", HttpMethod.GET),

    /**
     * iMoutai send verification code url
     */
    SEND_CODE_URL("https://app.moutai519.com.cn/xhr/front/user/register/vcode", HttpMethod.POST),

    /**
     * iMoutai login url
     */
    LOGIN_URL("https://app.moutai519.com.cn/xhr/front/user/register/login", HttpMethod.POST),

    /**
     * iMoutai reverse url
     */
    REVERSE_URL("https://app.moutai519.com.cn/xhr/front/mall/reservation/add", HttpMethod.POST);

    private final String url;

    private final HttpMethod method;

    MoutaiUrl(String url, HttpMethod method) {
        this.url = url;
        this.method = method;
    }

    public String url() {
        if (this == ITEM_URL) {
            return url + LocalDate.now()
                    .atStartOfDay()
                    .toInstant(Constants.SHANGHAI_OFFSET)
                    .toEpochMilli();
        }

        return url;
    }

    public HttpMethod method() {
        return method;
    }
}
