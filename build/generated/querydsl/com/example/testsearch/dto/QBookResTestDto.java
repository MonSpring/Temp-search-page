package com.example.testsearch.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.testsearch.dto.QBookResTestDto is a Querydsl Projection type for BookResTestDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBookResTestDto extends ConstructorExpression<BookResTestDto> {

    private static final long serialVersionUID = 1650248840L;

    public QBookResTestDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> author, com.querydsl.core.types.Expression<String> publisher, com.querydsl.core.types.Expression<? extends java.util.Date> publicationYear, com.querydsl.core.types.Expression<Long> isbn, com.querydsl.core.types.Expression<? extends java.util.Date> regDate, com.querydsl.core.types.Expression<Long> libcode, com.querydsl.core.types.Expression<String> bookCount) {
        super(BookResTestDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, java.util.Date.class, long.class, java.util.Date.class, long.class, String.class}, id, title, author, publisher, publicationYear, isbn, regDate, libcode, bookCount);
    }

}

