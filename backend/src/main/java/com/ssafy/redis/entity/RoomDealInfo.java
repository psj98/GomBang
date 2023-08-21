package com.ssafy.redis.entity;

import com.ssafy.redis.RoomDealRedisStoreDto;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@RedisHash("roomDealInfo")
@AllArgsConstructor
public class RoomDealInfo implements Serializable {

    @Id
    private String jibunAddress;

    private List<RoomDealRedisStoreDto> roomDealRedisStoreDtoList;
}
