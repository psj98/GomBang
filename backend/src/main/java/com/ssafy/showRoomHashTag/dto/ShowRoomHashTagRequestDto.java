package com.ssafy.showRoomHashTag.dto;

import com.ssafy.hashTag.dto.HashTagRegisterRequestDto;
import com.ssafy.showRoom.dto.ShowRoomRegisterRequestDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ShowRoomHashTagRequestDto {

    @NotNull
    private ShowRoomRegisterRequestDto showRoomRegisterRequestDto; // 곰방봐

    @NotNull
    private HashTagRegisterRequestDto hashTagRegisterRequestDto; // 해시태그
}
