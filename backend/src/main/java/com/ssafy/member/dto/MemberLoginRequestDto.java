package com.ssafy.member.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequestDto {

    @NonNull
    private String channelId;

    @NonNull
    private String email;

    @NonNull
    private String gender;

}
