package io.hydrocarbon.moutai.param.request.moutai;

import io.hydrocarbon.moutai.param.PageParam;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QueryMoutaiUserParam extends PageParam {

    private String mobile;
}
