package com.example.testsearch.repository;

import com.example.testsearch.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTaskRepository extends JpaRepository<Books, Long> {

    @Query(value = "SELECT " +
            "isbn FROM Books " +
            "GROUP BY isbn " +
            "ORDER BY isbn DESC " +
            "LIMIT 500"
            , nativeQuery = true)
    List<Long> findTopByOrderByIsbnDesc();

    @Query(value = "SELECT " +
            "isbn FROM Books " +
            "WHERE isbn < :deatilMinIsbn " +
            "GROUP BY isbn " +
            "ORDER BY isbn DESC " +
            "LIMIT 500"
            , nativeQuery = true)
    List<Long> findTopByIsbnLessThanOrderByIsbnDesc(Long deatilMinIsbn);
}
