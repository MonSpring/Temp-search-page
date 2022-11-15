package com.example.testsearch.dto;

import com.example.testsearch.entity.Books;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class BookResTestDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Date publicationYear;
    private Long isbn;
    private Date regDate;
    private Long libcode;
    private String bookCount;


    @QueryProjection
    public BookResTestDto(Long id, String title, String author, String publisher, Date publicationYear, Long isbn, Date regDate, Long libcode, String bookCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.regDate = regDate;
        this.libcode = libcode;
        this.bookCount = bookCount;
    }

    @Builder
    public BookResTestDto(Books book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publicationYear = book.getPublicationYear();
        this.isbn = book.getIsbn();
        this.regDate = book.getRegDate();
        this.bookCount = book.getBookCount();
    }

}
