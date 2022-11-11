package com.example.testsearch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter @Setter
@Entity
public class Books {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String publisher;

    @Column(name = "publication_year")
    private Date publicationYear;

    @Column
    private Long isbn;

    @Column(name = "book_count")
    private String bookCount;

    @Column(name = "lend_out_book_count")
    private int lendOutBookCount;

    @Column(name = "reg_date")
    private Date regDate;

    @JoinColumn(name = "libcode")
    @ManyToOne(fetch = FetchType.LAZY)
    private Librarys librarys;

    @Builder
    public Books(Long id, String title, String author, String publisher,
                 Date publicationYear, Long isbn, String bookCount, int lendOutBookCount,
                 Date regDate, Librarys librarys) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.bookCount = bookCount;
        this.lendOutBookCount = lendOutBookCount;
        this.regDate = regDate;
        this.librarys = librarys;
    }
}
