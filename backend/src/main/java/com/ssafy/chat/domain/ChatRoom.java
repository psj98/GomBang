package com.ssafy.chat.domain;

import com.ssafy.chat.dto.ChatCreateRequestDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

// STOMP를 통해 pub/sub를 사용하면 구독자 관리가 알아서 된다.
// 따라서 따로 세션 관리를 하는 코드를 작성할 필요도 없고,
// 메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현할 필요 X
@Data
@Entity
public class ChatRoom {
    @Id
    @Column(name = "room_id", columnDefinition = "BINARY(16)")
    private UUID id; // 채팅방 아이디

    @Column(columnDefinition = "BINARY(16)")
    private UUID grantorId; // 양도자 아이디

    @Column(columnDefinition = "BINARY(16)")
    private UUID assigneeId; // 양수자 아이디

    private Integer roomDealId; // 매물글 아이디

    public ChatRoom create(ChatCreateRequestDto chatCreateRequestDto) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID();
        chatRoom.assigneeId = chatCreateRequestDto.getAssigneeId();
        chatRoom.grantorId  = chatCreateRequestDto.getGrantorId();
        chatRoom.roomDealId = chatCreateRequestDto.getRoomDealId();

        return chatRoom;
    }
}
