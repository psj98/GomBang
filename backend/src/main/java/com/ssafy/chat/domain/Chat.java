package com.ssafy.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {
    // 메시지 타임 : 입장, 채팅
    // 메시지 타입에 따라서 동작하는 구조가 달라진다.
    // 입장과 퇴장 (Enter, Leave)의 경우 입장/퇴장 이벤트 처리가 실행되고,
    // TALK는 말 그대로 내용이 채팅방을 SUB하고 있는 모든 클라이언트에게 전달됨
    public enum MessageType {
        ENTER, TALK, LEAVE;
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 채팅 발송 시간
}
