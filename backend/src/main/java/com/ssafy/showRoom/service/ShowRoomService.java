package com.ssafy.showRoom.service;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import com.ssafy.showRoom.domain.ShowRoom;
import com.ssafy.showRoom.dto.ShowRoomRegisterRequestDto;
import com.ssafy.showRoom.dto.ShowRoomResponseDto;
import com.ssafy.showRoom.repository.ShowRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowRoomService {

    private final ShowRoomRepository showRoomRepository;
    private final RoomDealRepository roomDealRepository;
    private final MemberRepository memberRepository;


    public ShowRoomResponseDto registerShowRoom(ShowRoomRegisterRequestDto showRoomRegisterRequestDto) throws BaseException {

        RoomDeal roomDeal = verifyRoomDeal(showRoomRegisterRequestDto.getRoomDealId());

        checkMemberAuthority(roomDeal, showRoomRegisterRequestDto.getMemberId());

        Member member = verifyMember(showRoomRegisterRequestDto.getMemberId());

        ShowRoom showRoom = ShowRoom.builder()
                .roomDeal(roomDeal)
                .member(member)
                .roadAddress(roomDeal.getRoadAddress())
                .jibunAddress(roomDeal.getJibunAddress())
                .station(roomDeal.getStation())
                .univ(roomDeal.getUniv())
                .registerTime(new Date(System.currentTimeMillis()))
                .dealStatus(roomDeal.getDealStatus())
                .build();

        showRoomRepository.save(showRoom);

        return new ShowRoomResponseDto(showRoom);
    }

    private RoomDeal verifyRoomDeal(Long roomDealId) {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealId);
        if (roomDealOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 매물입니다.");
        }

        return roomDealOptional.get();
    }

    private void checkMemberAuthority(RoomDeal roomDeal, UUID memberId) {
        if(!roomDeal.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없는 사용자 입니다.");
        }
    }

    private Member verifyMember(UUID memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다.");
        }
        return memberOptional.get();
    }


}
