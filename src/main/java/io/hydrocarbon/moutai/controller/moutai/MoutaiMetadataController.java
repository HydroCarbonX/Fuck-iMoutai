package io.hydrocarbon.moutai.controller.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiItem;
import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import io.hydrocarbon.moutai.param.Response;
import io.hydrocarbon.moutai.param.response.moutai.MoutaiItemResponse;
import io.hydrocarbon.moutai.param.response.moutai.MoutaiShopResponse;
import io.hydrocarbon.moutai.service.MetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/moutai/metadata")
@RequiredArgsConstructor
@Slf4j
public class MoutaiMetadataController {

    private final MetadataService metadataService;

    @GetMapping("/shop")
    public Response<Map<String, Map<String, Map<String, List<MoutaiShopResponse>>>>> getAllShop() {
        List<MoutaiShop> allShop = metadataService.getAllShop();

        // 聚合到省份，市，区
        Map<String, Map<String, Map<String, List<MoutaiShopResponse>>>> provinceCityAreaMap = allShop.stream()
                .collect(Collectors.groupingBy(MoutaiShop::getProvinceName,
                        Collectors.groupingBy(MoutaiShop::getCityName,
                                Collectors.groupingBy(MoutaiShop::getDistrictName,
                                        Collectors.mapping(MoutaiShopResponse::fromEntity, Collectors.toList())))));
        return Response.success(provinceCityAreaMap);
    }

    @GetMapping("/item")
    public Response<List<MoutaiItemResponse>> getAllItem() {
        List<MoutaiItem> allItem = metadataService.getAllItem();
        List<MoutaiItemResponse> itemResponse = allItem.stream()
                .map(MoutaiItemResponse::fromEntity)
                .toList();
        return Response.success(itemResponse);
    }
}
