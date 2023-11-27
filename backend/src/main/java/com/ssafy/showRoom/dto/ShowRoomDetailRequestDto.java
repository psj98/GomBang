package com.ssafy.showRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomDetailRequestDto {

    private UUID memberId;
    private Integer showRoomId;
}
