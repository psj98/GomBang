package com.ssafy.live.service;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.live.domain.RtcRoom;
import com.ssafy.live.domain.RtcRoomMap;
import com.ssafy.live.domain.WebSocketMessage;
import com.ssafy.live.dto.RtcCreateRequestDto;
import com.ssafy.live.dto.RtcCreateResponseDto;
import com.ssafy.live.dto.RtcUserCountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RtcService {
    public RtcCreateResponseDto createRtcRoom(RtcCreateRequestDto rtcCreateRequestDto) {

        String roomId = rtcCreateRequestDto.getRoomId();

        RtcRoom exist = RtcRoomMap.getInstance().getRooms().get(roomId);

        if(exist != null) return new RtcCreateResponseDto(exist);

        RtcRoom room = RtcRoom.builder()
                .roomId(roomId)
                .build();

        room.setUserList(new HashMap<>());

        RtcRoomMap.getInstance().getRooms().put(roomId, room);

        return new RtcCreateResponseDto(room);
    }

    public void deleteRtcRoom(String roomId) {
        RtcRoom exist = RtcRoomMap.getInstance().getRooms().remove(roomId);
    }

    public Map<String, WebSocketSession> getClients(RtcRoom room) {
        return room.getUserList() == null ? null : room.getUserList();
    }

    public Map<String, WebSocketSession> addClient(RtcRoom room, String userId, WebSocketSession session) {
        Map<String, WebSocketSession> userList = room.getUserList();
        userList.put(userId, session);
        return userList;
    }

    public void removeClientById(RtcRoom room, String userId) {
        room.getUserList().remove(userId);
    }

    public RtcUserCountResponseDto findUserCount(String roomId) throws BaseException {
        RtcRoom room = RtcRoomMap.getInstance().getRooms().get(roomId);
        if(room != null) {
            return new RtcUserCountResponseDto(room.getUserList().size() > 1);
        }
        else {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_RTC_ROOM);
        }
    }
}
