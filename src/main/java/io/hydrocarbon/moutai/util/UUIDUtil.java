package io.hydrocarbon.moutai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-17
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class UUIDUtil {

    private static final long SERVER_ID = (1L & 255) << 56;

    // 北京时间
    private static long startSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

    private static int incrementedValue = 0;

    private static final Lock LOCK = new ReentrantLock();

    // 每当 incrementedValue 超过 2^24 时，将 incrementedValue 重置为 0
    static {
        Thread.ofVirtual().start(() -> {
            // noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Thread.sleep(Duration.ofMinutes(10));
                } catch (InterruptedException e) {
                    log.error("Thread.sleep(Duration.ofMinutes(10)) was interrupted", e);
                    Thread.currentThread().interrupt();
                }
                if (incrementedValue >= (1 << 23)) {
                    incrementedValue = 0;
                    startSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                }
            }
        });
    }

    public static long generate() {
        try {
            LOCK.lock();
            return SERVER_ID + (startSecond << 24) + incrementedValue++;
        } finally {
            LOCK.unlock();
        }
    }
}
