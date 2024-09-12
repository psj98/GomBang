package com.ssafy.likeShowRoom.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 좋아요 등록
 */
@Getter
public class LikeShowRoomRegisterRequestDto {

    @NotNull
    private UUID memberId; // 유저

    @NotNull
    private Integer showRoomId; // 곰방봐
}
