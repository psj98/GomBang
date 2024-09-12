package com.ssafy.starRoomDeal.dto;

import com.ssafy.member.domain.Member;
import com.ssafy.roomDeal.domain.RoomDeal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class StarRoomDealRegisterResponseDto {

    @NotNull
    private Member member;

    @NotNull
    private RoomDeal roomDeal;
}
