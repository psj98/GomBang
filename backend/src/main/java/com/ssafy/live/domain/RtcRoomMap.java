package com.ssafy.live.domain;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class RtcRoomMap {
    private static RtcRoomMap rtcRoomMap = new RtcRoomMap();
    private Map<String, RtcRoom> rooms = new LinkedHashMap<>();

    private RtcRoomMap(){};

    public static RtcRoomMap getInstance() {
        return rtcRoomMap;
    }
}
