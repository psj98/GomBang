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
    private List<String> fileUrls;
    private boolean checkLike;

    public ShowRoomDetailResponseDto(ShowRoom showroom, List<HashTag> hashTag, List<String> fileUrls) {
        this.showRoom = showroom;
        this.hashTag = hashTag;
        this.fileUrls = fileUrls;
    }

    public ShowRoomDetailResponseDto addCheckLike(boolean checkLike) {
        this.checkLike = checkLike;
        return this;
    }
}
