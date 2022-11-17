package com.example.testsearch.repository;

import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.dto.QBookResTestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.testsearch.entity.QBooks.books;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookResTestDto> searchByFullTextBooleanTest(String word, String mode, int page, int size, Pageable pageable, String field) {
        BooleanBuilder builder=new BooleanBuilder();
        if(field.equals("isbn")) {
            List<BookResTestDto> result = queryFactory
                    .select(
                            new QBookResTestDto(
                                    books.id,
                                    books.title,
                                    books.author,
                                    books.publisher,
                                    books.publicationYear,
                                    books.isbn,
                                    books.regDate,
                                    books.librarys.libcode,
                                    books.bookCount
                            )
                    )
                    .from(books)
                    .where(books.isbn.eq(Long.valueOf(word)))
                    .offset(page)
                    .limit(size)
                    .fetch();

            Long count = queryFactory
                    .select(books.count())
                    .from(books)
                    .where(books.isbn.eq(Long.valueOf(word)))
                    .fetchOne();
            int countTemp = Integer.parseInt(String.valueOf(count));

            return new PageImpl<>(result,pageable,countTemp);
        }

        NumberTemplate booleanTemplate= Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", bookEq(field), word);

        NumberTemplate booleanTemplate2= Expressions.numberTemplate(Double.class,
                "function('match2',{0},{1})", bookEq(field), word);

        if(Objects.equals(mode, "boolean")) {
            builder.and(booleanTemplate.gt(1));
        } else {
            builder.and(booleanTemplate2.gt(1));
        }
        List<BookResTestDto> result = queryFactory
                .select(
                        new QBookResTestDto(
                                books.id,
                                books.title,
                                books.author,
                                books.publisher,
                                books.publicationYear,
                                books.isbn,
                                books.regDate,
                                books.librarys.libcode,
                                books.bookCount
                        )
                )
                .from(books)
                .where(builder)
                .offset(page)
                .limit(size)
                .fetch();

        Long count = queryFactory
                .select(books.count())
                .from(books)
                .where(builder)
                .fetchOne();
        int countTemp = Integer.parseInt(String.valueOf(count));

        return new PageImpl<>(result,pageable,countTemp);

    }

    private Object bookEq(String field) {
        if(field.equals("title")){
            return books.title;
        }
        else if(field.equals("author")){
            return books.author;
        }
        else if(field.equals("publisher")){
            return books.publisher;
        }
        else if(field.equals("isbn")){
            return books.isbn;
        }
        return null;
    }


}
