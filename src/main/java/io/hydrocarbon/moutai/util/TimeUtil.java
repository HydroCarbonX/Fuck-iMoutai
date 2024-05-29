package io.hydrocarbon.moutai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class TimeUtil {

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
}
