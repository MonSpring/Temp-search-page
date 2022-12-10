package com.example.testsearch.service;

import com.example.testsearch.entity.ElasticBooks;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ElasticBooksResDto {

    private Long id;

    private String title;

    private String author;

    private String publisher;

    private Long isbn;

    private String bookCount;

    public ElasticBooksResDto(ElasticBooks elasticBooks) {
        this.id = elasticBooks.getBook_id();
        this.title = elasticBooks.getTitle();
        this.author = elasticBooks.getAuthor();
        this.publisher = elasticBooks.getPublisher();
        this.isbn = elasticBooks.getIsbn();
        this.bookCount = elasticBooks.getBookCount();
    }
}
