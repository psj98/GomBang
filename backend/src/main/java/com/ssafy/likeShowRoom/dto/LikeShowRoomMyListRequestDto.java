package com.ssafy.likeShowRoom.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 내가 좋아요 한 목록
 */
@Getter
public class LikeShowRoomMyListRequestDto {

    @NotNull
    private UUID memberId;
}
