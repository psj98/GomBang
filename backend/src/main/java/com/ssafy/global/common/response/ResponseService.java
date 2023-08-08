package com.ssafy.global.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    /**
     * 성공 응답 메소드 (간단버전)
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
     * 성공 응답 메소드 (코드 입력 버전)
     *
     * @param code - 결과 코드
     * @param message - 결과 메시지
     * @param data - 결과 데이터
     * @return BaseResponse - 응답 객체
     */
    public <T>BaseResponse<Object> getDetailSuccessResponse(int code, String message, T data) {
        return BaseResponse.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 
     * @param code - 에러 코드
     * @param message - 에러 메시지
     * @return BaseResponse - 응답 객체
     */
    public <T>BaseResponse<Object> getFailureResponse(int code, String message) {
        return BaseResponse.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }
}
