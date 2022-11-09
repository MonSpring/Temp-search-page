package com.example.testsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository  extends JpaRepository<Books, Long> {

    @Query(value = "SELECT * FROM books WHERE MATCH(title, author) AGAINST (:searchText IN BOOLEAN MODE)", nativeQuery = true)
    List<Books> searchByFullText(@Param("searchText") String searchText);

    Page<Books> findAll(Pageable pageable);

}
