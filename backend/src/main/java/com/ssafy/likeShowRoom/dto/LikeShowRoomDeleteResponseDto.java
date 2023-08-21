package com.ssafy.likeShowRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 좋아요 해제
 */
@Getter
@AllArgsConstructor
public class LikeShowRoomDeleteResponseDto {

    @NotNull
    private UUID memberId;

    @NotNull
    private Integer showRoomId;
}
