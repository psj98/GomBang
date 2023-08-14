package com.ssafy.showRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomDeleteRequestDto implements Serializable {

    @NotNull
    private Integer showRoomId;

    @NotNull
    private UUID memberId;
}
