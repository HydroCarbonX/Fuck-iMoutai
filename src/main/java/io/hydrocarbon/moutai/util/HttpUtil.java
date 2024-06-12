package io.hydrocarbon.moutai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hydrocarbon.moutai.enums.MoutaiUrl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * 使用 Java 的 HttpClient 封装的 HTTP 请求工具类
 *
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@SuppressWarnings("unused")
public final class HttpUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /*
     * 配置 ObjectMapper，忽略未知属性
     */
    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 发送 GET 请求
     *
     * @param url           请求地址
     * @param typeReference 返回类型
     * @param <T>           返回数据
     * @return 返回数据
     */
    public static <T> T get(String url, TypeReference<T> typeReference) {
        return get(url, typeReference, null);
    }

    /**
     * 发送 GET 请求
     *
     * @param url           请求地址
     * @param typeReference 返回类型
     * @param headers       请求头
     * @param <T>           返回数据
     * @return 返回数据
     */
    public static <T> T get(String url,
                            TypeReference<T> typeReference,
                            Map<String, String> headers) {

        try (HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build()) {

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20));

            if (Objects.nonNull(headers) && !headers.isEmpty()) {
                headers.forEach(requestBuilder::header);
            }

            HttpRequest httpRequest = requestBuilder
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            return MAPPER.readValue(response.body(), typeReference);
        } catch (IOException e) {
            log.error("http request error", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("http request interrupted", e);
        }
        return null;
    }

    public static <T> T post(String url, TypeReference<T> typeReference) {
        return post(url, typeReference, null);
    }

    public static <T> T post(String url, TypeReference<T> typeReference,
                             Map<String, String> headers) {
        return post(url, typeReference, headers, null);
    }

    /**
     * 发送 POST 请求
     *
     * @param url           请求地址
     * @param typeReference 返回类型
     * @param headers       请求头
     * @param body          请求体
     * @param <T>           返回数据
     * @param <E>           请求体数据
     * @return 返回数据
     */
    public static <T, E> T post(String url, TypeReference<T> typeReference,
                                Map<String, String> headers,
                                E body) {
        try (HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build()) {

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20));

            if (Objects.nonNull(headers) && !headers.isEmpty()) {
                headers.forEach(requestBuilder::header);
            }

            if (Objects.nonNull(body)) {
                requestBuilder.POST(HttpRequest.BodyPublishers
                        .ofString(MAPPER.writeValueAsString(body)));
            } else {
                requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
            }

            HttpRequest httpRequest = requestBuilder.build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return MAPPER.readValue(response.body(), typeReference);
        } catch (IOException e) {
            log.error("http request error", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("http request interrupted", e);
        }
        return null;
    }

    /**
     * 发送请求
     *
     * @param urlEnum       请求地址枚举（包含请求地址和请求类型）
     * @param typeReference 返回类型
     * @param headers       请求头
     * @param body          请求体
     * @param <T>           返回数据
     * @param <E>           请求体数据
     * @return 返回数据
     */
    public static <T, E> T request(MoutaiUrl urlEnum,
                                   TypeReference<T> typeReference,
                                   Map<String, String> headers,
                                   E body) {
        if (urlEnum.method().equals(GET)) {
            return get(urlEnum.url(), typeReference, headers);
        } else if (urlEnum.method().equals(POST)) {
            return post(urlEnum.url(), typeReference, headers, body);
        } else {
            log.error("unsupported http method: {}", urlEnum.method());
            return null;
        }
    }
}
