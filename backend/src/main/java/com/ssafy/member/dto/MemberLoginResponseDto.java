package com.ssafy.member.dto;

import com.ssafy.member.domain.Member;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Member member;
}
