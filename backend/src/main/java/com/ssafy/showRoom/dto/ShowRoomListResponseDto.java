package com.ssafy.showRoom.dto;

import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ShowRoomListResponseDto {

    @NotNull
    private ShowRoom showRoom; // 곰방봐

    @NotNull
    private boolean checkLike; // 좋아요 여부 확인
}
