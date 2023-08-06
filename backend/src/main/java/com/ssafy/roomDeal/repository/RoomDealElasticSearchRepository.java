package com.ssafy.roomDeal.repository;

import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomDealElasticSearchRepository extends ElasticsearchRepository<RoomDealSearchDto, String> {
}
