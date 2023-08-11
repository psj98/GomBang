package com.ssafy.global.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    private boolean isSuccess;

    private String message;

    private int code;

    private T data;

    @Builder
    public BaseResponse(boolean isSuccess, String message, int code, T data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.code = code;
        this.data = data;
    }

}
