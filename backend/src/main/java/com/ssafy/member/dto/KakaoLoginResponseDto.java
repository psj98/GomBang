package com.ssafy.member.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponseDto {

    @NotNull
    private String channelId;

    @NotNull
    private String email;

    @NotNull
    private String gender;

}
