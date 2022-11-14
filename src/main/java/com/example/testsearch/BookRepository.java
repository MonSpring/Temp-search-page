package com.example.testsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository  extends JpaRepository<Books, Long> {

    // 검색 기능   ========================================(개선의 여지 있음)====================================

    // Title
    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in boolean mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchTitleFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(title) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchTitleFullTextCount(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in natural language mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchNativeTitleFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativeTitleFullTextCount(@Param("word") String word);

    // Author
    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word in boolean mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchAuthorFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(author) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchAuthorFullTextCount(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word in natural language mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchNativeAuthorFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(author) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativeAuthorFullTextCount(@Param("word") String word);

    // Publisher
    @Query(value = "SELECT * FROM books WHERE MATCH(Publisher) AGAINST(:word in boolean mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchPublisherFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(Publisher) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchPublisherFullTextCount(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(Publisher) AGAINST(:word in natural language mode) LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchNativePublisherFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(Publisher) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativePublisherFullTextCount(@Param("word") String word);

    // isbn
    @Query(value = "SELECT * FROM books WHERE isbn=:word LIMIT :size OFFSET :page", nativeQuery = true)
    List<Books> searchIsbn(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE isbn=:word ", nativeQuery = true)
    int searchIsbnTextCount(@Param("word") String word);

    // 검색 기능 ====================================================================================================

    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word IN BOOLEAN MODE)", nativeQuery = true)
    List<Books> searchByFullTextBoolean(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)", nativeQuery = true)
    List<Books> onlyWordSearchByFullText(@Param("word") String word);

    Page<Books> findAll(Pageable pageable);

    @Query("select b from Books b where b.title LIKE %:word%")
    List<Books> findByWord(@Param("word") String word);

//    @Query(value = "SELECT b FROM books.b WHERE ")
//    List<Books> findAll(@Param("page") int page, @Param("offset") int offset, @Param("limit") int limit);

//    int page, int offset, int limit

//    @Query(value = "SELECT * FROM books WHERE isbn LIKE :isbn", nativeQuery = true)
//    List<Books> findAllByIsbnContains(@Param("isbn") String isbn);

    @Query(value = "SELECT * FROM books WHERE isbn LIKE :isbn", nativeQuery = true)
    boolean findAllByIsbnContains(@Param("isbn") String isbn);
}
