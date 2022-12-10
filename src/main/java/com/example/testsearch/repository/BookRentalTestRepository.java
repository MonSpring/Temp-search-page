package com.example.testsearch.repository;

import com.example.testsearch.entity.BookRentalTest;
import com.example.testsearch.entity.Books;
import com.example.testsearch.entity.MemberTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRentalTestRepository extends JpaRepository<BookRentalTest,Long> {

    boolean existsByBookAndMember(Books book, MemberTest member);

    @Modifying
    void deleteByBookAndMember(Books book, MemberTest member);

    Long countByBook(Books book);

    @Modifying
    @Query(value = "insert into book_rental_test (book, member) values (:book, :member)", nativeQuery = true)
    void saveBookRentalTest(Books book, MemberTest member);
}
