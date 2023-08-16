package com.ssafy.showRoom.dto;

import com.ssafy.hashTag.domain.HashTag;
import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomDetailResponseDto {

    private ShowRoom showRoom;
    private List<HashTag> hashTag;
    private boolean checkLike;
}
