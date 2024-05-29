package io.hydrocarbon.moutai.util;

import io.hydrocarbon.moutai.service.MetadataService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@SpringBootTest
class HttpUtilTest {

    @Resource
    private MetadataService metadataService;

    @Test
    void testGet() {
        metadataService.saveShopList();
    }

    @Test
    void get() {
        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(dayTime);
    }
}
