package com.example.testsearch.repository;

import com.example.testsearch.entity.ElasticBooks;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticBooksRepository extends ElasticsearchRepository<ElasticBooks, String> {

    int countAllByTitleContains(String word);

    int countAllByAuthorContains(String word);

    int countAllByPublisherContains(String word);

    int countAllByIsbnContains(String word);

    int countAllByTitleKeywordContains(String word);

    int countAllByAuthorKeywordContains(String word);

    int countAllByPublisherKeywordContains(String word);

    List<ElasticBooks> findAllByTitleContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByAuthorContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByPublisherContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByIsbnContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByTitleKeywordContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByAuthorKeywordContains(String word, Pageable pageable);

    List<ElasticBooks> findAllByPublisherKeywordContains(String word, Pageable pageable);
}


