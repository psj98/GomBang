package com.ssafy.roomDeal.repository;

import com.ssafy.roomDeal.domain.RoomDeal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDealRedisRepository extends CrudRepository<RoomDeal, Long> {

}
