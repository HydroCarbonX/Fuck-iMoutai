package io.hydrocarbon.moutai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class TimeUtil {

    private static final Random RANDOM = new Random();

    private static final DateTimeFormatter HH_MM_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalTime toLocalTime(String time) {
        time = StringUtils.trimAllWhitespace(time);
        if (!StringUtils.hasText(time)) {
            return null;
        }

        try {
            return LocalTime.parse(time, HH_MM_TIME_FORMATTER);
        } catch (Exception e) {
            log.error("parse time error, date: {}", time, e);
            return null;
        }
    }

    public static Long nowTimestamp() {
        return OffsetDateTime.now().toInstant().toEpochMilli();
    }

    public static OffsetDateTime randomNextReserveTime() {
        OffsetDateTime nextDateTime = OffsetDateTime.now().plusDays(1);

        int hour = 9;
        // 1 - 58 分钟
        int minute = RANDOM.nextInt(58) + 1;
        return nextDateTime.withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    }
}
