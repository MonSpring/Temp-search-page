package com.example.testsearch.repository;

import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.QBookResTestDto;
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

import static com.example.testsearch.QBooks.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookResTestDto> searchByFullTextBooleanTest(String word, int page, int size, Pageable pageable) {
        BooleanBuilder builder=new BooleanBuilder();

        NumberTemplate booleanTemplate= Expressions.numberTemplate(Double.class,
                "function('match',{0},{1},{2})", books.title, word);

        builder.and(booleanTemplate.gt(0));

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
                .where()
                .offset(page)
                .limit(size)
                .fetch();


        return new PageImpl<>(result,pageable,result.size());

    }


}
