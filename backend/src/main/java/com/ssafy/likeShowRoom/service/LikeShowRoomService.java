package com.ssafy.likeShowRoom.service;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.likeShowRoom.domain.LikeShowRoom;
import com.ssafy.likeShowRoom.domain.LikeShowRoomId;
import com.ssafy.likeShowRoom.dto.*;
import com.ssafy.likeShowRoom.repository.LikeShowRoomRepository;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.showRoom.domain.ShowRoom;
import com.ssafy.showRoom.repository.ShowRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeShowRoomService {

    private final LikeShowRoomRepository likeShowRoomRepository;
    private final MemberRepository memberRepository;
    private final ShowRoomRepository showRoomRepository;

    /**
     * 좋아요 등록
     *
     * @param likeShowRoomRegisterRequestDto
     * @return
     * @throws BaseException
     */
    public LikeShowRoomResponseDto registerLikeShowRoom(LikeShowRoomRegisterRequestDto likeShowRoomRegisterRequestDto) throws BaseException {
        UUID memberId = likeShowRoomRegisterRequestDto.getMemberId();
        Integer showRoomId = likeShowRoomRegisterRequestDto.getShowRoomId();

        ShowRoom showRoom = verifyShowRoom(showRoomId); // 곰방봐 id 체크
        Member member = verifyMember(memberId); // memberId 체크

        LikeShowRoomId likeShowRoomId = new LikeShowRoomId(memberId, showRoomId);

        LikeShowRoom likeShowRoom = LikeShowRoom.builder()
                .id(likeShowRoomId)
                .member(member)
                .showRoom(showRoom)
                .build();

        Optional<LikeShowRoom> likeShowRoomOptional = likeShowRoomRepository.findById(likeShowRoomId);
        if (!likeShowRoomOptional.isPresent()) {
            likeShowRoomRepository.save(likeShowRoom); // 좋아요 등록
        } else {
            throw new BaseException(BaseResponseStatus.LIKE_SHOW_ROOM_REGISTER_FAILED);
        }

        return new LikeShowRoomResponseDto(member, showRoom);
    }

    /**
     * 곰방봐 id 체크
     *
     * @param showRoomId
     * @return
     * @throws BaseException
     */
    private ShowRoom verifyShowRoom(Integer showRoomId) throws BaseException {
        Optional<ShowRoom> showRoomOptional = showRoomRepository.findById(showRoomId);
        if (showRoomOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_SHOW_ROOM_ID);
        }

        return showRoomOptional.get();
    }

    /**
     * memberId 체크
     *
     * @param memberId
     * @return
     * @throws BaseException
     */
    private Member verifyMember(UUID memberId) throws BaseException {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER);
        }
        return memberOptional.get();
    }

    /**
     * 좋아요 해제
     */
    public LikeShowRoomDeleteResponseDto deleteLikeShowRoom(LikeShowRoomDeleteRequestDto likeShowRoomDeleteRequestDto) throws BaseException {
        UUID memberId = likeShowRoomDeleteRequestDto.getMemberId();
        Integer showRoomId = likeShowRoomDeleteRequestDto.getShowRoomId();

        verifyShowRoom(showRoomId); // 곰방봐 id 체크
        verifyMember(memberId); // memberId 체크

        LikeShowRoomId likeShowRoomId = new LikeShowRoomId(memberId, showRoomId);

        Optional<LikeShowRoom> likeShowRoomOptional = likeShowRoomRepository.findById(likeShowRoomId);
        if (likeShowRoomOptional.isPresent()) {
            likeShowRoomRepository.deleteById(likeShowRoomId); // 좋아요 해제
        } else {
            throw new BaseException(BaseResponseStatus.LIKE_SHOW_ROOM_DELETE_FAILED);
        }

        return new LikeShowRoomDeleteResponseDto(memberId, showRoomId);
    }

    /**
     * 내가 좋아요한 목록
     */
    public LikeShowRoomMyListResponseDto getMyLikeShowRoomList(LikeShowRoomMyListRequestDto likeShowRoomMyListRequestDto) throws BaseException {
        UUID memberId = likeShowRoomMyListRequestDto.getMemberId();

        verifyMember(memberId); // memberId 체크

        List<Integer> myLikeShowRoomIdList = likeShowRoomRepository.getMyLikeShowRoomId(memberId);

        return new LikeShowRoomMyListResponseDto(myLikeShowRoomIdList);
    }
}
