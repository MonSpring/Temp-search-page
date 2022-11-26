package com.example.testsearch.repository;

import com.example.testsearch.entity.MemberLoginInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberLoginInfoRepository extends ElasticsearchRepository<MemberLoginInfo, String> {
    List<MemberLoginInfo> findAllByLoginTimeBetweenOrderByLoginTimeDesc(LocalDateTime startTime, LocalDateTime endTime);
}
