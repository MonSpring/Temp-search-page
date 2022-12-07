package com.example.testsearch.repository;

import com.example.testsearch.entity.BookRentals;
import com.example.testsearch.entity.Books;
import com.example.testsearch.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRentalRepository extends JpaRepository<BookRentals,Long> {

    boolean existsByBookAndMember(Books book, Member member);

    void deleteByBookAndMember(Books book, Member member);

    Long countByBook(Books book);


}
