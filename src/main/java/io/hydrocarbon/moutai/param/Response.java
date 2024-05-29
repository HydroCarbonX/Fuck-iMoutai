package io.hydrocarbon.moutai.param;

import lombok.Data;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-21
 */
@Data
public class Response<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static <T> Response<T> fail() {
        return fail("fail");
    }

    public static <T> Response<T> fail(String message) {
        return fail(400, message);
    }

    public static <T> Response<T> fail(Integer code, String message) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public boolean hasData() {
        return data != null;
    }
}
