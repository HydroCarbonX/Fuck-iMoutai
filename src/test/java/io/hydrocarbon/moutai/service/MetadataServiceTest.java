package io.hydrocarbon.moutai.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@SpringBootTest
class MetadataServiceTest {

    @Resource
    private MetadataService metadataService;

    @Resource
    private MoutaiService moutaiService;

    @Test
    void testSaveItemList() {
        metadataService.refreshMetadata();
    }

    @Test
    void reserveAll() {
        moutaiService.reserveAll();
    }
}
