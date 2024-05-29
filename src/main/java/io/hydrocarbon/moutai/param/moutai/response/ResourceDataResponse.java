package io.hydrocarbon.moutai.param.moutai.response;

import lombok.Data;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@Data
public class ResourceDataResponse {

    private String md5;

    private String url;

    private Long size;

    private Integer version;
}
