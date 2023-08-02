package com.ssafy.member.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDto {

    @NonNull
    private UUID id;

    @Builder.Default
    @NonNull
    private String name = "사용자";

    @NonNull
    private String channelId;

    @NonNull
    private String nickname = "로그인한 곰돌이";

    @NonNull
    private String email;

    @NonNull
    private String gender;

}
