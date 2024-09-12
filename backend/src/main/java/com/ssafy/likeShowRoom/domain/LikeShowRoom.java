package com.ssafy.likeShowRoom.domain;

import com.ssafy.member.domain.Member;
import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeShowRoom implements Serializable {

    // 복합키 매핑
    @EmbeddedId
    private LikeShowRoomId id;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "BINARY(16)")
    private Member member; // 유저

    @MapsId("showRoomId")
    @ManyToOne
    @JoinColumn(name = "show_room_id")
    private ShowRoom showRoom; // 곰방봐
}
