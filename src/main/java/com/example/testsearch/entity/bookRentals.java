package com.example.testsearch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class bookRentals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_rental_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Books books;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private Date rentalDate;

    @Column
    private Date returnDate;

    @Builder
    public bookRentals(Long id, Books books, Member member, Date rentalDate, Date returnDate) {

        this.id = id;
        this.books = books;
        this.member = member;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }
}
