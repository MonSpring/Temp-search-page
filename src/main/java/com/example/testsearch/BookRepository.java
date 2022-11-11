package com.example.testsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository  extends JpaRepository<Books, Long> {

    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word IN BOOLEAN MODE)", nativeQuery = true)
    List<Books> searchByFullTextBoolean(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)", nativeQuery = true)
    List<Books> searchByFullText(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in natural language mode) LIMIT :size OFFSET :page" , nativeQuery = true)
    List<Books> searchByFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)" , nativeQuery = true)
    int searchByFullTextCount(@Param("word") String word);

    Page<Books> findAll(Pageable pageable);

    @Query("select b from Books b where b.title LIKE %:word%")
    List<Books> findByWord(@Param("word") String word);

//    @Query(value = "SELECT b FROM books.b WHERE ")
//    List<Books> findAll(@Param("page") int page, @Param("offset") int offset, @Param("limit") int limit);

//    int page, int offset, int limit
}
