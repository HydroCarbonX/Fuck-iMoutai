package io.hydrocarbon.moutai.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class String {
        public static final java.lang.String EMPTY = "";

        public static final java.lang.String COMMA = ",";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Moutai {

        public static final java.lang.String DEVICE_ID = "4c16a678-17ff-11ef-81c3-0242ac130002";

        public static final java.lang.String APP_VERSION = "1.6.5";

        public static final java.lang.String SESSION_ID = "sessionId";

        public static final Map<java.lang.String, java.lang.String> DEFAULT_HEADER_MAP = Map.of(
                "MT-Device-ID", DEVICE_ID,
                "MT-APP-Version", APP_VERSION,
                "User-Agent", "iOS;16.3;Apple;?unrecognized?",
                "Content-Type", "application/json");
    }
}
