package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderBooks is a Querydsl query type for OrderBooks
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderBooks extends EntityPathBase<OrderBooks> {

    private static final long serialVersionUID = -441991900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderBooks orderBooks = new QOrderBooks("orderBooks");

    public final QBooks books;

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrders orders;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QOrderBooks(String variable) {
        this(OrderBooks.class, forVariable(variable), INITS);
    }

    public QOrderBooks(Path<? extends OrderBooks> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderBooks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderBooks(PathMetadata metadata, PathInits inits) {
        this(OrderBooks.class, metadata, inits);
    }

    public QOrderBooks(Class<? extends OrderBooks> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.books = inits.isInitialized("books") ? new QBooks(forProperty("books"), inits.get("books")) : null;
        this.orders = inits.isInitialized("orders") ? new QOrders(forProperty("orders"), inits.get("orders")) : null;
    }

}

