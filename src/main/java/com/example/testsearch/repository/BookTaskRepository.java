package com.example.testsearch.repository;

import com.example.testsearch.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface BookTaskRepository extends JpaRepository<Books, Long> {

    @Query(value = "SELECT " +
            "isbn FROM Books " +
            "GROUP BY isbn " +
            "ORDER BY isbn DESC " +
            "LIMIT 5"
            , nativeQuery = true)
    Set<Long> findTopByOrderByIsbnDesc();

    @Query(value = "SELECT " +
            "isbn FROM Books " +
            "WHERE isbn < :maxIsbn " +
            "GROUP BY isbn " +
            "ORDER BY isbn DESC " +
            "LIMIT 5"
            , nativeQuery = true)
    Set<Long> findTopByIsbnLessThanOrderByIsbnDesc(Long maxIsbn);
}
