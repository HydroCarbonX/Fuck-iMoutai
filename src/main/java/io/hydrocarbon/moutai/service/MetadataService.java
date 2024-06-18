package io.hydrocarbon.moutai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.hydrocarbon.moutai.constant.Constants;
import io.hydrocarbon.moutai.entity.moutai.MoutaiConfig;
import io.hydrocarbon.moutai.entity.moutai.MoutaiItem;
import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import io.hydrocarbon.moutai.enums.MoutaiUrl;
import io.hydrocarbon.moutai.param.Response;
import io.hydrocarbon.moutai.param.response.moutai.ItemDetailResponse;
import io.hydrocarbon.moutai.param.response.moutai.ResourceDataResponse;
import io.hydrocarbon.moutai.param.response.moutai.ShopDetailResponse;
import io.hydrocarbon.moutai.property.MoutaiProperty;
import io.hydrocarbon.moutai.repository.moutai.MoutaiConfigRepository;
import io.hydrocarbon.moutai.repository.moutai.MoutaiItemRepository;
import io.hydrocarbon.moutai.repository.moutai.MoutaiShopRepository;
import io.hydrocarbon.moutai.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetadataService {

    private final MoutaiProperty moutaiProperty;

    private final MoutaiShopRepository moutaiShopRepository;

    private final MoutaiItemRepository moutaiItemRepository;

    private final MoutaiConfigRepository moutaiConfigRepository;

    public String getResourceList(String urlKey) {
        Response<Map<String, ResourceDataResponse>> result = HttpUtil.get(MoutaiUrl.RESOURCE_URL.url(),
                new TypeReference<>() {
                });


        if (Objects.isNull(result) || !result.hasData()) {
            return null;
        }

        ResourceDataResponse resourceDataResponse = result.getData()
                .getOrDefault(urlKey, new ResourceDataResponse());
        return resourceDataResponse.getUrl();
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshMetadata() {
        log.info("===== Start to refresh metadata on {} =====", OffsetDateTime.now());
        // 1. 删除数据
        moutaiShopRepository.deleteAllInBatch();
        moutaiShopRepository.flush();

        moutaiItemRepository.deleteAllInBatch();
        moutaiItemRepository.flush();

        // 2. 刷入新数据
        saveShopList();
        saveItemList();
        log.info("===== End to refresh metadata on {} =====", OffsetDateTime.now());
    }

    public void saveShopList() {
        // 调用获取资源拿到 shop 获取的 URL
        String shopUrl = getResourceList(moutaiProperty.getShopUrlKey());

        // 获取 Shop 数据
        Map<String, ShopDetailResponse> shopDetailResponseMap = HttpUtil.get(shopUrl,
                new TypeReference<>() {
                });

        if (Objects.isNull(shopDetailResponseMap) || shopDetailResponseMap.isEmpty()) {
            log.error("shop list is null");
            return;
        }

        // 转换为实体类
        List<MoutaiShop> moutaiShopList = shopDetailResponseMap.values().stream()
                .map(ShopDetailResponse::toEntity)
                .toList();

        // 入库
        moutaiShopRepository.saveAllAndFlush(moutaiShopList);
    }

    public void saveItemList() {
        // 获取 Item 数据
        Response<ItemDetailResponse> response = HttpUtil.request(MoutaiUrl.ITEM_URL,
                new TypeReference<>() {
                },
                null,
                null);

        if (Objects.isNull(response) || !response.hasData()) {
            log.error("item list is null");
            return;
        }

        ItemDetailResponse itemDetailResponse = response.getData();

        // 获取 sessionId
        Long sessionId = itemDetailResponse.getSessionId();

        // 刷新 sessionId
        refreshSessionId(sessionId);

        log.info("sessionId: {}", sessionId);

        // 转换为实体类
        List<MoutaiItem> moutaiItemList = itemDetailResponse.getItemList().stream()
                .filter(Objects::nonNull)
                .map(ItemDetailResponse.Item::toEntity)
                .toList();

        // 入库
        moutaiItemRepository.saveAllAndFlush(moutaiItemList);
    }

    public List<MoutaiShop> getAllShop() {
        return moutaiShopRepository.findAll();
    }

    public List<MoutaiItem> getAllItem() {
        return moutaiItemRepository.findAll();
    }

    private void refreshSessionId(Long sessionId) {
        moutaiConfigRepository.findByKey(Constants.Moutai.SESSION_ID).ifPresentOrElse(moutaiConfig -> {
            if (!sessionId.equals(Long.parseLong(moutaiConfig.getValue()))) {
                moutaiConfig.setValue(sessionId.toString());
                moutaiConfigRepository.save(moutaiConfig);
            }
        }, () -> {
            MoutaiConfig moutaiConfig = new MoutaiConfig();
            moutaiConfig.setKey(Constants.Moutai.SESSION_ID);
            moutaiConfig.setValue(sessionId.toString());
            moutaiConfigRepository.save(moutaiConfig);
        });
    }
}
