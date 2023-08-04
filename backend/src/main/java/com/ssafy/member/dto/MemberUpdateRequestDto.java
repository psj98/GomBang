package com.ssafy.member.dto;

import com.ssafy.member.domain.Gender;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequestDto {

    private UUID id;

    private String name;
}
