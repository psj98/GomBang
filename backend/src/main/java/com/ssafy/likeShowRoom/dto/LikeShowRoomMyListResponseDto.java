package com.ssafy.likeShowRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 내가 좋아요 한 목록
 */
@Getter
@AllArgsConstructor
public class LikeShowRoomMyListResponseDto {

    private List<Integer> showRoomIdList;
}
