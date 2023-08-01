package com.ssafy.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserJoinRequestDto {

    @NonNull
    private String channelId;

    @NonNull
    private String email;

    @NonNull
    private String userName;

}
