package io.hydrocarbon.moutai.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Disabled("用来跑测试数据，并非单元测试功能。")
@SpringBootTest
class MetadataServiceTest {

    @Resource
    private MetadataService metadataService;

    @Resource
    private MoutaiService moutaiService;

    @Test
    void testRefreshMetadata() {
        // 无异常
        Assertions.assertDoesNotThrow(() -> metadataService.refreshMetadata());
    }

    @Test
    void reserveAll() {
        boolean result = moutaiService.reserveAll(true);

        Assertions.assertTrue(result);
    }
}
