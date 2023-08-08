package com.ssafy.elasticsearch.repository;

import com.ssafy.elasticsearch.dto.RoomDealSearchResponseDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomDealElasticSearchRepository extends ElasticsearchRepository<RoomDealSearchResponseDto, String> {
}
