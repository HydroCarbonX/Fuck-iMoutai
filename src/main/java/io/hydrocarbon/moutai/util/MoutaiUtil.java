package io.hydrocarbon.moutai.util;

import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import io.hydrocarbon.moutai.entity.moutai.MoutaiUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoutaiUtil {

    public static String getDeviceId() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    public static Map<String, String> getDefaultHeaderMap(String deviceId,
                                                          String appVersion) {
        Map<String, String> map = new HashMap<>();
        map.put("MT-Device-ID", deviceId);
        map.put("MT-APP-Version", appVersion);
        map.put("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        map.put("Content-Type", "application/json");
        return map;
    }

    public static Map<String, String> getReserveHeaderMap(MoutaiShop moutaiShop, MoutaiUser user, String appVersion) {
        Map<String, String> defaultHeaderMap = getDefaultHeaderMap(user.getDeviceId(), appVersion);
        defaultHeaderMap.put("MT-Lat", moutaiShop.getLat().toPlainString());
        defaultHeaderMap.put("MT-Lng", moutaiShop.getLng().toPlainString());
        defaultHeaderMap.put("MT-Token", user.getToken());
        defaultHeaderMap.put("MT-Info", "028e7f96f6369cafe1d105579c5b9377");
        return defaultHeaderMap;
    }
}
