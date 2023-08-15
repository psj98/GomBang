package com.ssafy.starRoomDeal.service;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import com.ssafy.starRoomDeal.domain.StarMemberMapping;
import com.ssafy.starRoomDeal.domain.StarRoomDeal;
import com.ssafy.starRoomDeal.domain.StarRoomDealId;
import com.ssafy.starRoomDeal.domain.StarRoomDealMapping;
import com.ssafy.starRoomDeal.dto.*;
import com.ssafy.starRoomDeal.repository.StarRoomDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StarRoomDealService {

    private final StarRoomDealRepository starRoomDealRepository;
    private final MemberRepository memberRepository;
    private final RoomDealRepository roomDealRepository;

    /**
     * 매물 글을 찜한다.
     *
     * @param starRoomDealRegisterRequestDto 찜 할 사용자의 UUID, 찜 할 매물 글의 id
     * @return 사용자 정보, 매물 글 정보
     * @throws BaseException
     */
    public StarRoomDealRegisterResponseDto registerStarRoomDeal(StarRoomDealRegisterRequestDto starRoomDealRegisterRequestDto) throws BaseException {
        UUID memberId = starRoomDealRegisterRequestDto.getMemberId();
        Long roomDealId = starRoomDealRegisterRequestDto.getRoomDealId();

        Member member = verifyMember(memberId); // 사용자 정보 확인
        RoomDeal roomDeal = verifyRoomDeal(roomDealId); // 매물 글 정보 확인

        StarRoomDealId starRoomDealId = new StarRoomDealId(memberId, roomDealId);

        StarRoomDeal starRoomDeal = StarRoomDeal.builder()
                .id(starRoomDealId)
                .member(member)
                .roomDeal(roomDeal)
                .build();

        Optional<StarRoomDeal> starRoomDealOptional = starRoomDealRepository.findById(starRoomDealId);
        if (!starRoomDealOptional.isPresent()) {
            starRoomDealRepository.save(starRoomDeal); // 찜 등록
        } else {
            throw new BaseException(BaseResponseStatus.STAR_ROOM_DEAL_REGISTER_FAILED);
        }

        return new StarRoomDealRegisterResponseDto(member, roomDeal);
    }

    /**
     * 매물 글에 등록한 찜을 취소한다.
     *
     * @param starRoomDealDeleteRequestDto 찜 할 사용자의 UUID, 찜 할 매물 글의 id
     * @return 사용자의 UUID, 매물 글의 id
     */
    public StarRoomDealDeleteResponseDto deleteStarRoomDeal(StarRoomDealDeleteRequestDto starRoomDealDeleteRequestDto) throws BaseException {
        UUID memberId = starRoomDealDeleteRequestDto.getMemberId();
        Long roomDealId = starRoomDealDeleteRequestDto.getRoomDealId();

        verifyMember(memberId); // 사용자 정보 확인
        verifyRoomDeal(roomDealId); // 매물 글 정보 확인

        StarRoomDealId starRoomDealId = new StarRoomDealId(memberId, roomDealId);

        Optional<StarRoomDeal> starRoomDealOptional = starRoomDealRepository.findById(starRoomDealId);
        if (starRoomDealOptional.isPresent()) {
            starRoomDealRepository.deleteById(starRoomDealId); // 찜 취소
        } else {
            throw new BaseException(BaseResponseStatus.STAR_ROOM_DEAL_DELETE_FAILED);
        }

        return new StarRoomDealDeleteResponseDto(memberId, roomDealId);
    }

    /**
     * 사용자가 찜한 매물 글 목록을 조회한다.
     *
     * @param memberId 사용자의 UUID
     * @return 사용자가 찜한 매물 글 리스트
     */
    public StarRoomDealMyListResponseDto getMyStarRoomDealList(UUID memberId) throws BaseException {
        verifyMember(memberId); // 사용자 정보 확인

        Optional<List<StarRoomDealMapping>> myStarRoomDealList = starRoomDealRepository.findAllByMemberId(memberId);

        return new StarRoomDealMyListResponseDto(myStarRoomDealList.get());
    }

    /**
     * 매물을 찜한 사용자 목록을 조회한다.
     *
     * @param roomDealId 매물 글의 id
     * @return 매물을 찜한 사용자 리스트
     */
    public StarRoomDealMemberListResponseDto getRoomDealStarredMemberList(Long roomDealId) throws BaseException {
        verifyRoomDeal(roomDealId); // 매물 글 정보 확인

        Optional<List<StarMemberMapping>> roomDealStarMemberList = starRoomDealRepository.findAllByRoomDealId(roomDealId);

        return new StarRoomDealMemberListResponseDto(roomDealStarMemberList.get());
    }

    /**
     * 사용자의 UUID를 기준으로 사용자 정보를 조회한다.
     *
     * @param memberId 사용자의 아이디
     * @return member 사용자 정보
     * @throws BaseException
     */
    private Member verifyMember(UUID memberId) throws BaseException {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER);
        }
    }

    /**
     * 매물 글의 id를 기준으로 매물 정보를 조회한다.
     *
     * @param roomDealId 매물의 아이디
     * @return roomDeal 매물 정보
     * @throws BaseException
     */
    private RoomDeal verifyRoomDeal(Long roomDealId) throws BaseException {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealId);

        if (roomDealOptional.isPresent()) {
            return roomDealOptional.get();
        } else {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }
    }

}
