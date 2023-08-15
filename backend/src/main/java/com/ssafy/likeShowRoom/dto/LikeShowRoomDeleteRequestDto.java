package com.ssafy.likeShowRoom.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 좋아요 해제
 */
@Getter
public class LikeShowRoomDeleteRequestDto {

    @NotNull
    private UUID memberId; // 유저

    @NotNull
    private Integer showRoomId; // 곰방봐
}
