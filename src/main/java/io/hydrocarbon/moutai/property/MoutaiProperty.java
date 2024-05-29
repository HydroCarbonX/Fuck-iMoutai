package io.hydrocarbon.moutai.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@ConfigurationProperties(prefix = "i-moutai.resource")
@Data
public class MoutaiProperty {

    private String shopUrlKey;

    private String salt;

    private String aesKey;

    private String aesIv;

    private String appVersion;
}
