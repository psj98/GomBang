package com.ssafy.elasticsearch.repository;

import com.ssafy.elasticsearch.dto.RoomDealSaveDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomDealElasticSearchRepository extends ElasticsearchRepository<RoomDealSaveDto, String> {
}
