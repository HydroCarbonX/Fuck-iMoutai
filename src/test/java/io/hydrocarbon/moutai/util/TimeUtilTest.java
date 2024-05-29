package io.hydrocarbon.moutai.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
class TimeUtilTest {

    @Test
    void testGet() {
        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(dayTime);
    }
}
