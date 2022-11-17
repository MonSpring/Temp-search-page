package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBooks is a Querydsl query type for Books
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBooks extends EntityPathBase<Books> {

    private static final long serialVersionUID = -356755326L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBooks books = new QBooks("books");

    public final StringPath author = createString("author");

    public final StringPath bookCount = createString("bookCount");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> isbn = createNumber("isbn", Long.class);

    public final NumberPath<Integer> lendOutBookCount = createNumber("lendOutBookCount", Integer.class);

    public final QLibrarys librarys;

    public final DateTimePath<java.util.Date> publicationYear = createDateTime("publicationYear", java.util.Date.class);

    public final StringPath publisher = createString("publisher");

    public final DateTimePath<java.util.Date> regDate = createDateTime("regDate", java.util.Date.class);

    public final StringPath title = createString("title");

    public QBooks(String variable) {
        this(Books.class, forVariable(variable), INITS);
    }

    public QBooks(Path<? extends Books> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBooks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBooks(PathMetadata metadata, PathInits inits) {
        this(Books.class, metadata, inits);
    }

    public QBooks(Class<? extends Books> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.librarys = inits.isInitialized("librarys") ? new QLibrarys(forProperty("librarys")) : null;
    }

}

