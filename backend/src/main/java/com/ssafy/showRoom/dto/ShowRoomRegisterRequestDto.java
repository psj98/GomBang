package com.ssafy.showRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class ShowRoomRegisterRequestDto {

    @NotNull
    private Long roomDealId;

    @NotNull
    private UUID memberId;

}
