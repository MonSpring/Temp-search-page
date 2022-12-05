package com.example.testsearch.dto;

import com.example.testsearch.entity.BookDetails;
import com.example.testsearch.entity.Books;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookDetailResDto {

    private Long id;
    private String title;
    private String author;
    private String description;
    private String thumbnail;
    private Long isbn;
    private Long bookCount;

    @Builder
    public BookDetailResDto(BookDetails bookDetails, Books books, Long bookCount) {
        this.id = books.getId();
        this.title = books.getTitle();
        this.author = books.getAuthor();
        this.description = bookDetails.getDescription();
        this.thumbnail = bookDetails.getThumbnail();
        this.isbn = books.getIsbn();
        this.bookCount = bookCount;
    }

}
