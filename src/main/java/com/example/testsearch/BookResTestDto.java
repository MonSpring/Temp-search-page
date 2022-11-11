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
    private Date publicationYear;
    private Long isbn;
    private Date regDate;
    private Librarys librarys;
    private String bookCount;

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

    public void update(Librarys librarys) {
        this.librarys = librarys;
    }

}
