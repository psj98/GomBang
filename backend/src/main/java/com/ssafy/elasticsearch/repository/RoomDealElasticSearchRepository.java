package com.ssafy.elasticsearch.repository;

import com.ssafy.elasticsearch.dto.RoomDealSaveDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDealElasticSearchRepository extends ElasticsearchRepository<RoomDealSaveDto, String> {
}
