package io.hydrocarbon.moutai.param.request.moutai;

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
public class SendCodeRequest {

    private String mobile;

    private String md5;

    private Long timestamp;
}
