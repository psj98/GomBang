package com.ssafy.elasticsearch.repository;

import com.ssafy.elasticsearch.dto.ShowRoomSaveDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRoomElasticSearchRepository extends ElasticsearchRepository<ShowRoomSaveDto, String> {
}
