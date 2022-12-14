package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookRentalTest is a Querydsl query type for BookRentalTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRentalTest extends EntityPathBase<BookRentalTest> {

    private static final long serialVersionUID = -47306553L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookRentalTest bookRentalTest = new QBookRentalTest("bookRentalTest");

    public final QBooks book;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMemberTest member;

    public QBookRentalTest(String variable) {
        this(BookRentalTest.class, forVariable(variable), INITS);
    }

    public QBookRentalTest(Path<? extends BookRentalTest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookRentalTest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookRentalTest(PathMetadata metadata, PathInits inits) {
        this(BookRentalTest.class, metadata, inits);
    }

    public QBookRentalTest(Class<? extends BookRentalTest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBooks(forProperty("book"), inits.get("book")) : null;
        this.member = inits.isInitialized("member") ? new QMemberTest(forProperty("member")) : null;
    }

}

