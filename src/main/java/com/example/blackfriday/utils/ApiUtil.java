package com.example.blackfriday.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ApiUtil {

    /**
     * Api 응답 성공 시 사용하는 메서드 응답 body를 담고있다.
     *
     * @param body
     * @return
     */
    public static <T> ApiSuccessResult<T> success(T body) {
        return new ApiSuccessResult<>("success", body);
    }


    /**
     * Api 응답 실패 시 사용하는 메서드 errorCode와 message를 담고있다.
     *
     * @param errorCode
     * @param mesasge
     * @return
     */
    public static <T> ApiErrorResult<T> error(int errorCode, T mesasge) {
        return new ApiErrorResult<>("fail", errorCode, mesasge);
    }


    /**
     * Api 성공 시 응답 형식
     *
     * @param <T>
     */
    @Getter
    @AllArgsConstructor
    public static class ApiSuccessResult<T> {

        private final String result;
        private final T body;
    }

    /**
     * Api 실패 시 응답 형식
     *
     * @param <T>
     */
    @Getter
    @AllArgsConstructor
    public static class ApiErrorResult<T> {

        private final String result;
        private final int errorCode;
        private final T message;
    }
}