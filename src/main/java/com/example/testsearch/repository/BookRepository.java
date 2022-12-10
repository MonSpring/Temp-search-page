package com.example.testsearch.repository;

import com.example.testsearch.dto.BookInfiniteRepoResDto;
import com.example.testsearch.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Books, Long>, BookRepositoryCustom {

    // Cursor Based InfinityScroll
    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE book_id <= :lastId ORDER BY book_id DESC LIMIT :limitSize", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchBookListForInfinityScroll(int lastId, int limitSize);
    //    book_id, title, author, publisher, book_count, isbn

    // Max Book Query
    @Query(value = "SELECT max(book_id) FROM books", nativeQuery = true)
    int searchInfinityCountMaxNum();

    // 검색 기능
    // Title
    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(title) AGAINST(:word in boolean mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchTitleFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(title) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchTitleFullTextCount(@Param("word") String word);

    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(title) AGAINST(:word in natural language mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchNativeTitleFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativeTitleFullTextCount(@Param("word") String word);

    // Author
    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(author) AGAINST(:word in boolean mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchAuthorFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(author) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchAuthorFullTextCount(@Param("word") String word);

    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(author) AGAINST(:word in natural language mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchNativeAuthorFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(author) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativeAuthorFullTextCount(@Param("word") String word);

    // Publisher
    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(Publisher) AGAINST(:word in boolean mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchPublisherFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(Publisher) AGAINST(:word in boolean mode)", nativeQuery = true)
    int searchPublisherFullTextCount(@Param("word") String word);

    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE MATCH(Publisher) AGAINST(:word in natural language mode) ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchNativePublisherFullText(@Param("word") String word, @Param("size") int size, @Param("page") int page);

    @Query(value = "SELECT count(1) FROM books WHERE MATCH(Publisher) AGAINST(:word in natural language mode)", nativeQuery = true)
    int searchNativePublisherFullTextCount(@Param("word") String word);

    // isbn
    @Query(value = "SELECT book_id, title, author, publisher, book_count, isbn FROM books WHERE isbn=:word ORDER BY book_id DESC LIMIT :size OFFSET :page", nativeQuery = true)
    List<BookInfiniteRepoResDto> searchIsbn(@Param("word") String word, @Param("size") int size, @Param("page") int page);

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


    // excel용
    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in boolean mode)", nativeQuery = true)
    List<Books> titleForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word in boolean mode)", nativeQuery = true)
    List<Books> authorForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(Publisher) AGAINST(:word in boolean mode)", nativeQuery = true)
    List<Books> publisherForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE isbn=:word", nativeQuery = true)
    List<Books> isbnForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(title) AGAINST(:word in natural language mode)", nativeQuery = true)
    List<Books> titleNatureForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(author) AGAINST(:word in natural language mode)", nativeQuery = true)
    List<Books> authorNatureForExcel(@Param("word") String word);

    @Query(value = "SELECT * FROM books WHERE MATCH(Publisher) AGAINST(:word in natural language mode)", nativeQuery = true)
    List<Books> publisherNatureForExcel(@Param("word") String word);

    @Query(value = "SELECT b.isbn FROM Books b WHERE b.id=:bookId")
    Long isbnFindById(Long bookId);

}
