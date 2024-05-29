package io.hydrocarbon.moutai.param.moutai.response;

import io.hydrocarbon.moutai.entity.moutai.MoutaiItem;
import lombok.Data;

import java.util.List;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Data
public class ItemDetailResponse {
    private Long sessionId;

    private List<Item> itemList;

    @Data
    public static class Item {
        private String itemCode;

        private String picture;

        private String pictureV2;

        private String title;

        private String content;

        private String jumpUrl;

        private Long checkTag;

        private Long count;

        public MoutaiItem toEntity() {
            MoutaiItem moutaiItem = new MoutaiItem();
            moutaiItem.setItemCode(this.itemCode);
            moutaiItem.setTitle(this.title);
            moutaiItem.setContent(this.content);
            moutaiItem.setPicture(this.picture);
            moutaiItem.setPictureV2(this.pictureV2);
            moutaiItem.setJumpUrl(this.jumpUrl);
            moutaiItem.setCheckTag(this.checkTag);
            moutaiItem.setCount(this.count);
            return moutaiItem;
        }
    }
}
