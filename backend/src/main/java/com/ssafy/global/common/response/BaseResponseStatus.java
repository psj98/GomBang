package com.ssafy.global.common.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum BaseResponseStatus implements Serializable {

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
    NOT_FOUND_MEMBER(false, 2103, "일치하는 사용자가 없습니다."),

    NOT_FOUND_KAKAO_CODE(false, 2201, "입력된 카카오 인가코드가 없습니다."),
    KAKAO_API_CALL_FAILED(false, 2202, "카카오 API 호출에 실패했습니다."),

    /**
     * RoomDeal
     * code: 3000번대
     */
    NOT_MATCHED_ROOM_DEAL_ID(false, 3001, "존재하지 않는 매물입니다."),
    ROOM_DEAL_REGISTER_FAILED(false, 3002, "매물등록에 실패했습니다."),
    ROOM_DEAL_UPDATE_FAILED(false, 3003, "매물 수정에 실패했습니다."),
    ROOM_DEAL_DELETE_FAILED(false, 3004, "매물 삭제에 실패했습니다."),

    NOT_MATCHED_ROOM_DEAL_OPTION_ID(false, 3005, "존재하지 않는 매물옵션입니다."),

    NOT_AUTHORIZED(false, 3050, "권한이 없는 사용자 입니다."),

    NO_SEARCH_RESULT(false, 3100, "검색 결과가 없습니다."),

    /**
     * Chat
     * code: 4000번대
     */
    NOT_FOUND_CHAT_ROOM(false, 4001, "일치하는 채팅방이 없습니다."),
    CHAT_MESSAGE_SAVE_FAILED(false, 4002, "채팅 메시지 저장에 실패했습니다."),
    CHATROOM_CONNECT_FAIL(false, 4003, "채팅방 연결에 실패했습니다"),
    NO_CHATROOM_LIST(false, 4004, "채팅 목록이 없습니다."),

    /**
     * ShowRoom
     * code: 5000번대
     */
    NOT_MATCHED_SHOW_ROOM_ID(false, 5001, "존재하지 않는 곰방봐입니다."),
    NOT_MATCHED_SHOW_ROOM_HASH_TAG_ID(false, 5002, "해당 곰방봐에 존재하지 않는 해시태그입니다."),

    /**
     * Notification
     * code: 6000번대
     */
    NOT_FOUND_NOTIFICATION(false, 6001, "일치하는 알림이 없습니다."),

    /**
     * LikeShowRoom
     * code: 7000번대
     */
    LIKE_SHOW_ROOM_REGISTER_FAILED(false, 7001, "이미 등록된 좋아요 입니다."),
    LIKE_SHOW_ROOM_DELETE_FAILED(false, 7002, "좋아요가 등록되어 있지 않습니다."),

    /**
     * S3
     * code: 8000번대
     */
    PUT_FILE_FAILED(false, 8001, "사진 업로드에 실패했습니다."),
    GET_FILE_URL_FAILED(false, 8002, "사진 경로 반환에 실패했습니다."),
    SAVE_FILE_FAILED(false, 8003, "사진 저장에 실패했습니다."),

    /**
     * StarRoomDeal
     * code: 9000번대
     */
    STAR_ROOM_DEAL_REGISTER_FAILED(false, 9001, "이미 등록된 찜 입니다."),
    STAR_ROOM_DEAL_DELETE_FAILED(false, 9002, "찜이 등록되어 있지 않습니다."),

    /**
     * WebRTC
     * code : 10000번대
     */
    NOT_FOUND_RTC_ROOM(false, 10001, "라이브 방을 찾을 수 없습니다.");


    private boolean isSuccess;
    private int code;
    private String message;

    private BaseResponseStatus(boolean isSuccess,  int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }



}