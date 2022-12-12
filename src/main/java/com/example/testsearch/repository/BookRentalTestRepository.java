package com.example.testsearch.repository;

import com.example.testsearch.entity.BookRentalTest;
import com.example.testsearch.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

public interface BookRentalTestRepository extends JpaRepository<BookRentalTest,Long> {

    @Query(value = "select count(1) from book_rental_test where id = :bookId and member_id = :memberId", nativeQuery = true)
    int existsByBookIdAndMemberId(Long bookId, Long memberId);

/*    @Query(value = "select count(1) from book_rental_test where id = :bookId", nativeQuery = true)
    Long countByBookId(Long bookId);*/

    @Lock(LockModeType.PESSIMISTIC_READ)
    Long countByBook(Books book);

    @Query(value = "select member_id from book_rental_test where id = :bookId limit :wrongCount", nativeQuery = true)
    List<Long> findWrongRental(Long bookId, Long wrongCount);

    @Modifying
    @Query(value = "delete from book_rental_test where id = :bookId and member_id = :memberId", nativeQuery = true)
    void deleteByBookIdAndMemberId(Long bookId, Long memberId);

    @Modifying
    @Query(value = "insert into book_rental_test (id, member_id) values (:bookId, :memberId)", nativeQuery = true)
    void saveBookRentalTest(Long bookId, Long memberId);
}
