package com.ssafy.global.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    public <T>BaseResponse<Object> getSuccessResponse(String message, T data) {
        return BaseResponse.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public <T>BaseResponse<Object> getFailureResponse(String message) {
        return BaseResponse.builder()
                .isSuccess(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }
}
