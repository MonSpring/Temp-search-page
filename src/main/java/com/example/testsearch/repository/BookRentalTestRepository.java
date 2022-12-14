package com.example.testsearch.repository;

import com.example.testsearch.entity.BookRentalTest;
import com.example.testsearch.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRentalTestRepository extends JpaRepository<BookRentalTest,Long> {

    @Query(value = "select count(1) from book_rental_test where id = :bookId and member_id = :memberId", nativeQuery = true)
    int existsByBookIdAndMemberId(Long bookId, Long memberId);

    Long countByBook(Books book);

    @Modifying
    @Query(value = "insert into book_rental_test (id, member_id) values (:bookId, :memberId)", nativeQuery = true)
    void saveBookRentalTest(Long bookId, Long memberId);
}
