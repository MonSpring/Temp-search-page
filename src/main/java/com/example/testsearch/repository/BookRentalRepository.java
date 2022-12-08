package com.example.testsearch.repository;

import com.example.testsearch.entity.BookRentals;
import com.example.testsearch.entity.Books;
import com.example.testsearch.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface BookRentalRepository extends JpaRepository<BookRentals,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByBookAndMember(Books book, Member member);

    void deleteByBookAndMember(Books book, Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Long countByBook(Books book);
}
