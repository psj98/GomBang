package com.ssafy.chat.service;

import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatRoomListResponseDto;
import com.ssafy.chat.dto.ChatRoomResponseDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
import com.ssafy.chat.repository.ChatRoomRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    /**
     *
     * @param chatCreateRequestDto - 양도자 Id, 양수자 Id, 매물 Id
     * @return ChatCreateResponseDto - 채팅방 Id
     */
    public ChatRoomResponseDto createChatRoom(ChatCreateRequestDto chatCreateRequestDto) {
        ChatRoomResponseDto response = new ChatRoomResponseDto();

        UUID assigneeId = chatCreateRequestDto.getAssigneeId();
        UUID grantorId = chatCreateRequestDto.getGrantorId();

        Member grantor = memberRepository.findById(grantorId).get();
        Member assignee = memberRepository.findById(assigneeId).get();

        Optional<ChatRoom> pastRoom = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor);

        if(pastRoom.isPresent()) {
            response.setRoomId(pastRoom.get().getId());
        } else {

            System.out.println("22222222");
            ChatRoom chatRoom = new ChatRoom().create(chatCreateRequestDto, grantor, assignee);
            chatRoomRepository.save(chatRoom);
            response.setRoomId(chatRoom.getId());
        }

        return response;
    }

    /**
     * 양도자, 양수자 Id로 채팅방 Id 조회
     *
     * @param chatGetIdRequestDto - 양도자 Id, 양수자 Id
     * @return ChatGetIdResponseDto - 채팅방 Id
     */
    public ChatRoomResponseDto getChatRoomId(ChatGetIdRequestDto chatGetIdRequestDto) throws BaseException {

        UUID assigneeId = chatGetIdRequestDto.getAssigneeId();
        UUID grantorId = chatGetIdRequestDto.getGrantorId();

        Member grantor = memberRepository.findById(grantorId).get();
        Member assignee = memberRepository.findById(assigneeId).get();

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor);

        if(chatRoom.isPresent()) {
            ChatRoomResponseDto response = new ChatRoomResponseDto();
            response.setRoomId(chatRoom.get().getId());
            return response;
        } else {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_CHAT_ROOM);
        }
    }

    /**
     *
     * @param memberId
     * @return List<ChatRoom>
     */
    public ChatRoomListResponseDto getChatRoomList(UUID memberId) throws BaseException {
        Member member = memberRepository.findById(memberId).get();
        List<ChatRoom> list = chatRoomRepository.findByAssigneeIdOrGrantorId(member, member);
        if(!list.isEmpty()) {
            ChatRoomListResponseDto response = new ChatRoomListResponseDto();
            response.setList(list);
            return response;
        } else {
            throw new BaseException(BaseResponseStatus.NO_CHATROOM_LIST);
        }
    }
}
