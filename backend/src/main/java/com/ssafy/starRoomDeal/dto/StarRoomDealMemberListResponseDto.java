package com.ssafy.starRoomDeal.dto;

import com.ssafy.member.domain.Member;
import com.ssafy.starRoomDeal.domain.StarMemberMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class StarRoomDealMemberListResponseDto {

    private List<StarMemberMapping> starMemberList;
}
