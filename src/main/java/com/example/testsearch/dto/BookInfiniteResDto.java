package com.example.testsearch.dto;

import com.example.testsearch.entity.Books;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

}
