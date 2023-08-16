package com.ssafy.live.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.service.ChatRoomService;
import com.ssafy.live.domain.RtcRoom;
import com.ssafy.live.domain.RtcRoomMap;
import com.ssafy.live.domain.WebSocketMessage;
import com.ssafy.live.service.RtcService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {

    private final RtcService rtcService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, RtcRoom> rooms = RtcRoomMap.getInstance().getRooms();

    // message type
    private static final String OFFER = "offer";
    private static final String ANSWER = "answer";
    private static final String ICE = "ice";
    private static final String JOIN = "join";
    private static final String LEAVE = "leave";

    // 연결이 끊어졌을 때 이벤트 처리
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("[ws] 세션 Close [{} {}]", status, session);
    }

    // 소켓 연결되었을 때 이벤트 처리
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sendMessage(session, new WebSocketMessage("Server", JOIN, Boolean.toString(!rooms.isEmpty()), null, null));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        try {
            // 웹 소켓으로부터 전달받은 메시지
            // 소켓쪽에서는 socket.send로 메시지를 발송한다 -> 참고로 JSON 형식으로 변환해서 전달해온다.
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            logger.debug("[ws] {} 타입 메세지 받음 from {}", message.getType(), message.getFrom());

            // 유저 UUID와 roomId를 저장
            String userUUID = message.getFrom();
            String roomId = message.getData();

            RtcRoom rtcRoom;
            // 메시지 타입에 따라서 서버에서 하는 역할이 달라진다
            switch (message.getType()) {

                // 클라이언트에게서 받은 메시지 타입에 따른 signal 프로세스
                case OFFER:
                case ANSWER:
                case ICE:
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();

                    logger.debug("[ws] Signal: {}", candidate != null ? candidate.toString().substring(0, 64) : sdp.toString().substring(0, 64));

                    rtcRoom = rooms.get(roomId);

                    if (rtcRoom != null) {
                        Map<String, WebSocketSession> clients = rtcService.getClients(rtcRoom);
                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  {
                            // send messages to all clients except current user
                            if (!client.getKey().equals(userUUID)) {
                                // select the same type to resend signal
                                sendMessage(client.getValue(),
                                        new WebSocketMessage(
                                                userUUID,
                                                message.getType(),
                                                roomId,
                                                candidate,
                                                sdp));
                            }
                        }
                    }
                    break;

                // identify user and their opponent
                case JOIN:
                    // message.data contains connected room id
                    logger.debug("[ws] {} has joined Room: #{}", userUUID, message.getData());
                    rtcRoom = RtcRoomMap.getInstance().getRooms().get(roomId);

                    // room 안에 있는 userList 에 유저 추가
                    rtcService.addClient(rtcRoom, userUUID, session);

                    /* 이 부분에서 session.getID 대신 roomID 를 사용하면 문제 생김*/
                    rooms.put(roomId, rtcRoom);
                    break;

                case LEAVE:
                    // message data contains connected room id
                    logger.info("[ws] {} is going to leave Room: #{}", userUUID, message.getData());

                    // roomID 기준 채팅방 찾아오기
                    rtcRoom = rooms.get(message.getData());

                    // room clients list 에서 해당 유저 삭제
                    // 1. room 에서 client List 를 받아와서 keySet 을 이용해서 key 값만 가져온 후 stream 을 사용해서 반복문 실행
                    Optional<String> client = rtcService.getClients(rtcRoom).keySet().stream()
                            // 2. 이때 filter - 일종의 if문 -을 사용하는데 entry 에서 key 값만 가져와서 userUUID 와 비교한다
                            .filter(clientListKeys -> StringUtils.equals(clientListKeys, userUUID))
                            // 3. 하여튼 동일한 것만 가져온다
                            .findAny();

                    // 만약 client 의  값이 존재한다면 - Optional 임으로 isPersent 사용 , null  아니라면 - removeClientByName 을 실행
                    client.ifPresent(userID -> rtcService.removeClientById(rtcRoom, userID));

                    // 테스트 (방에 인원이 없으면 방 삭제)
                    if(rtcService.getClients(rtcRoom).size() == 0)
                        rtcService.deleteRtcRoom(rtcRoom.getRoomId());

                    logger.debug("삭제 완료 [{}] ",client);
                    break;

                // something should be wrong with the received message, since it's type is unrecognizable
                default:
                    logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
                    // handle this if needed
            }

        } catch(IOException e) {
            logger.debug("An error occured: {}", e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try{
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch(IOException e) {
            logger.debug("에러 발생: {}", e.getMessage());
        }
    }
}
