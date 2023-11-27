package com.ssafy.redis.repository;

import com.ssafy.redis.entity.RoomDealInfo;
import com.ssafy.roomDeal.domain.RoomDealOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomDealRedisRepository extends CrudRepository<RoomDealInfo, String> {

}
