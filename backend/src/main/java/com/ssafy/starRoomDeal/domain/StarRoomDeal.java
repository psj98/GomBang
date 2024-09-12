package com.ssafy.starRoomDeal.domain;

import com.ssafy.member.domain.Member;
import com.ssafy.roomDeal.domain.RoomDeal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarRoomDeal {

    @EmbeddedId
    private StarRoomDealId id;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "BINARY(16)")
    private Member member;

    @MapsId("roomDealId")
    @ManyToOne
    @JoinColumn(name = "room_deal_id")
    private RoomDeal roomDeal;
}
