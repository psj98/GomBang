package com.ssafy.global.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    /**
     * 성공 응답 메소드
     * 
     * @param message - 결과 메시지
     * @param data - 결과 데이터
     * @return BaseResponse - 응답 객체
     */
    public <T>BaseResponse<Object> getSuccessResponse(String message, T data) {
        return BaseResponse.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 성공 Status 메소드
     *
     * @param status - Custom한 Status
     * @return BaseResponse - 응답 객체
     */
    public <T>BaseResponse<Object> getSuccessStatusResponse(BaseResponseStatus status, T data) {
        return BaseResponse.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .data(data)
                .build();
    }
    
    /**
     * 실패 Status 메소드
     *
     * @param status - Custom한 Status
     * @return BaseResponse - 응답 객체
     */
    public <T>BaseResponse<Object> getFailureResponse(BaseResponseStatus status) {
        return BaseResponse.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();
    }
}
