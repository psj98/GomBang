package com.ssafy.global.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공했습니다."),



    /**
     * Member
     * code: 2000번대
     */

    LOGIN_SUCCESS(true, 2001, "로그인에 성공했습니다."),
    JOIN_SUCCESS(true, 2002, "회원가입에 성공했습니다."),
    JOINED_BUT_NO_NAME(true, 2003, "이전에 회원가입했지만 이름이 입력되지 않은 사용자입니다."),

    LOGIN_FAILED(false, 2101, "로그인에 실패했습니다."),
    MEMBER_NAME_UPDATE_FAILED(false, 2102, "사용자 이름 수정에 실패했습니다."),

    /**
     * RoomDeal
     * code: 3000번대
     */

    NOT_MATCHED_ROOM_DEAL_ID(false, 3001, "존재하지 않는 매물입니다."),
    ROOM_DEAL_REGISTER_FAILED(false, 3002, "매물등록에 실패했습니다."),
    ROOM_DEAL_UPDATE_FAILED(false, 3003, "매물 수정에 실패했습니다."),
    ROOM_DEAL_DELETE_FAILED(false, 3004, "매물 삭제에 실패했습니다."),


    NOT_AUTHORIZED(false, 3050, "권한이 없는 사용자 입니다."),

    NO_SEARCH_RESULT(false, 3100, "검색 결과가 없습니다."),

    /**
     * Chat
     * code: 4000번대
     */
    NOT_FOUND_CHAT_ROOM(false, 4001, "일치하는 채팅방이 없습니다."),
    CHAT_MESSAGE_SAVE_FAILED(false, 4002, "채팅 메시지 저장에 실패했습니다.");




    /**
     * ShowRoom
     * code: 5000번대
     */





    private boolean isSuccess;
    private int code;
    private String message;

    private BaseResponseStatus(boolean isSuccess,  int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}