package io.hydrocarbon.moutai.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {

    /**
     * 页码
     */
    @Builder.Default
    Integer pageNo = 0;

    /**
     * 每页大小
     */
    @Builder.Default
    Integer pageSize = 10;

    public Pageable toPageable() {
        return PageRequest.of(pageNo, pageSize,
                Sort.by(Sort.Order.desc("createdAt")));
    }
}
