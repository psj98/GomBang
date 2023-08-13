package com.ssafy.hashTag.repository;

import com.ssafy.hashTag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository  extends JpaRepository<HashTag, Integer> {

    @Query(value = "SELECT COUNT(hashTagName) FROM HashTag WHERE hashTagName = ?1")
    Long exist(String hashTagName);
}
