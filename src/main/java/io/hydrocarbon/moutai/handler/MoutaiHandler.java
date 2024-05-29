package io.hydrocarbon.moutai.handler;

import io.hydrocarbon.moutai.service.MetadataService;
import io.hydrocarbon.moutai.service.MoutaiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MoutaiHandler {

    private final MetadataService metadataService;

    private final MoutaiService moutaiService;

    @Async
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void refreshMetadata() {
        log.info("===== Start to refresh metadata on {} =====", OffsetDateTime.now());
        try {
            metadataService.refreshMetadata();
        } catch (Exception e) {
            log.error("Failed to refresh metadata", e);
        }
        log.info("===== End to refresh metadata on {} =====", OffsetDateTime.now());
    }

    @Async
    @Scheduled(cron = "21 5 9 * * ?")
    public void reserveAllUser() {
        log.info("===== Start to reserve all user on {} =====", OffsetDateTime.now());
        try {
            boolean success = moutaiService.reserveAll();
            if (!success) {
                log.error("Failed to reserve all user");
            }
        } catch (Exception e) {
            log.error("Failed to reserve all user", e);
        }
        log.info("===== End to reserve all user on {} =====", OffsetDateTime.now());
    }
}
