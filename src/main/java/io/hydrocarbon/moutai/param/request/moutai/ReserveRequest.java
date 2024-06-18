package io.hydrocarbon.moutai.param.request.moutai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ReserveRequest {
    private Long userId;

    private Long sessionId;

    private String actParam;

    private String shopId;

    private List<ItemInfo> itemInfoList;

    public String getAesEncodeString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", String.valueOf(userId));
            map.put("sessionId", String.valueOf(sessionId));
            map.put("shopId", shopId);
            map.put("itemInfoList", itemInfoList);
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("ReserveRequest getAesEncodeString error", e);
            return null;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private String itemId;

        private Integer count;
    }
}
