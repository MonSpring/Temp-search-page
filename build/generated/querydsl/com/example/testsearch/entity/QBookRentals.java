package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookRentals is a Querydsl query type for BookRentals
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRentals extends EntityPathBase<BookRentals> {

    private static final long serialVersionUID = -806055810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookRentals bookRentals = new QBookRentals("bookRentals");

    public final QTimestamped _super = new QTimestamped(this);

    public final QBooks book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QBookRentals(String variable) {
        this(BookRentals.class, forVariable(variable), INITS);
    }

    public QBookRentals(Path<? extends BookRentals> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookRentals(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookRentals(PathMetadata metadata, PathInits inits) {
        this(BookRentals.class, metadata, inits);
    }

    public QBookRentals(Class<? extends BookRentals> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBooks(forProperty("book"), inits.get("book")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

