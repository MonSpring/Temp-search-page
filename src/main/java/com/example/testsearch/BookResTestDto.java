package com.example.testsearch;

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
    private Date publication_year;

    private String bookCount;
    private Long isbn;
    private Date reg_date;
    private Librarys librarys;

    @Builder
    public BookResTestDto(Books book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publication_year = book.getPublicationYear();
        this.bookCount = book.getBookCount();
        this.isbn = book.getIsbn();
        this.reg_date = book.getRegDate();
    }

    public void update(Librarys librarys) {
        this.librarys = librarys;
    }
}
