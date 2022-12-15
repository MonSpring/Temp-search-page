package com.example.testsearch.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.testsearch.dto.QLibraryResDto is a Querydsl Projection type for LibraryResDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QLibraryResDto extends ConstructorExpression<LibraryResDto> {

    private static final long serialVersionUID = -1224540696L;

    public QLibraryResDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> author, com.querydsl.core.types.Expression<String> publisher, com.querydsl.core.types.Expression<String> bookCount, com.querydsl.core.types.Expression<Long> isbn, com.querydsl.core.types.Expression<String> libName) {
        super(LibraryResDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, long.class, String.class}, id, title, author, publisher, bookCount, isbn, libName);
    }

}

