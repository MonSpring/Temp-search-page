package com.example.testsearch.repository;

import com.example.testsearch.entity.MemberLoginInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberLoginInfoRepository extends ElasticsearchRepository<MemberLoginInfo, String> {
    int countAllByLoginTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<MemberLoginInfo> findAllByLoginTimeBetweenOrderByLoginTimeDesc(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    List<MemberLoginInfo> findAllByLoginTimeBetweenOrderByLoginTimeDesc(LocalDateTime startTime, LocalDateTime endTime);
}
