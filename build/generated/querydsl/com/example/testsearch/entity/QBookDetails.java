package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBookDetails is a Querydsl query type for BookDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookDetails extends EntityPathBase<BookDetails> {

    private static final long serialVersionUID = -341222671L;

    public static final QBookDetails bookDetails = new QBookDetails("bookDetails");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> isbn = createNumber("isbn", Long.class);

    public final StringPath thumbnail = createString("thumbnail");

    public QBookDetails(String variable) {
        super(BookDetails.class, forVariable(variable));
    }

    public QBookDetails(Path<? extends BookDetails> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookDetails(PathMetadata metadata) {
        super(BookDetails.class, metadata);
    }

}

