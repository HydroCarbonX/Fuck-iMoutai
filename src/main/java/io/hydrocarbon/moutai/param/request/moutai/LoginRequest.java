package io.hydrocarbon.moutai.param.request.moutai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String mobile;

    @JsonProperty("vCode")
    private String vCode;

    private String md5;

    private Long timestamp;

    @JsonProperty("MT-App-Version")
    private String mtAppVersion;
}
