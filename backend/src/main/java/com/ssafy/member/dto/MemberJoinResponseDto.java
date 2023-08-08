package com.ssafy.member.dto;

import com.ssafy.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinResponseDto {

    private UUID id;
}
