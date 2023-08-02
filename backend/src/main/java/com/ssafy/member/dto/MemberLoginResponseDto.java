package com.ssafy.member.dto;

import com.ssafy.member.domain.Member;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
public class MemberLoginResponseDto {

    @NotNull
    private Member member;
}
