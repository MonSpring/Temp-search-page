package com.example.testsearch.dto;

import com.example.testsearch.entity.Books;
import lombok.Builder;
import lombok.Data;

@Data
public class BookInfiniteResDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String bookCount;
    private Long isbn;

    @Builder
    public BookInfiniteResDto(Books book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.bookCount = book.getBookCount();
        this.isbn = book.getIsbn();
    }

    public BookInfiniteResDto(Long id, String title, String author, String publisher, String bookCount, Long isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.bookCount = bookCount;
        this.isbn = isbn;
    }
}
