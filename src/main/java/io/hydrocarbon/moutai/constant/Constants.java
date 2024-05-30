package io.hydrocarbon.moutai.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final ZoneOffset SHANGHAI_OFFSET = ZoneOffset.ofHours(8);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class String {
        public static final java.lang.String EMPTY = "";

        public static final java.lang.String COMMA = ",";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Moutai {

        public static final java.lang.String SESSION_ID = "sessionId";

    }
}
