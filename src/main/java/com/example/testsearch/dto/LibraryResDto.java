package com.example.testsearch.dto;

import com.example.testsearch.entity.Librarys;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LibraryResDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String bookCount;
    private Long isbn;
    private String libName;

    @QueryProjection
    public LibraryResDto(Long id, String title, String author, String publisher, String bookCount, Long isbn, String libName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.bookCount = bookCount;
        this.isbn = isbn;
        this.libName = libName;
    }
}
