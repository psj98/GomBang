package com.ssafy.chat.domain;

import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.member.domain.Member;
import lombok.Data;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member grantorId; // 양도자 아이디

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member assigneeId; // 양수자 아이디

    @Column
    private Integer roomDealId; // 매물글 아이디

    public ChatRoom create(ChatCreateRequestDto chatCreateRequestDto, Member grantorId, Member assigneeId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID();
        chatRoom.assigneeId = assigneeId;
        chatRoom.grantorId  = grantorId;
        chatRoom.roomDealId = chatCreateRequestDto.getRoomDealId();

        return chatRoom;
    }
}
