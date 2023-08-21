package com.ssafy.live.dto;

import com.ssafy.live.domain.RtcRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RtcCreateResponseDto {
    @NotNull
    RtcRoom room;
}
