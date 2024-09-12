package com.ssafy.likeShowRoom.dto;

import com.ssafy.member.domain.Member;
import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LikeShowRoomResponseDto {

    @NotNull
    private Member member;

    @NotNull
    private ShowRoom showRoom;
}
