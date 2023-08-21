package com.ssafy.showRoom.dto;

import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomResponseDto {

    private ShowRoom showRoom;

    private List<String> fileUrls;

    public ShowRoomResponseDto(ShowRoom showRoom) {
        this.showRoom = showRoom;
    }

    public ShowRoomResponseDto addFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;

        return this;
    }
}
