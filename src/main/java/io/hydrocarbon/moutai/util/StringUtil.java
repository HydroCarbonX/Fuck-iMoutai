package io.hydrocarbon.moutai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

    public static List<String> split(String str, String separator) {
        if (Objects.isNull(str) || str.isEmpty()) {
            return List.of();
        }

        return Arrays.asList(str.split(separator));
    }
}
