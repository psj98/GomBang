package com.ssafy.roomDeal.dto;

import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealResponseDto implements Serializable {

    @NotNull
    private RoomDeal roomDeal;

    @NotNull
    private RoomDealOption roomDealOption;

    private List<String> fileUrls;

    public RoomDealResponseDto(RoomDeal roomDeal, RoomDealOption roomDealOption) {
        this.roomDeal = roomDeal;
        this.roomDealOption = roomDealOption;
    }

    public RoomDealResponseDto addFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;

        return this;
    }

}
