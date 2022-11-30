package com.example.testsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailResDto {

    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private Long isbn;
    private String bookCount;

}
