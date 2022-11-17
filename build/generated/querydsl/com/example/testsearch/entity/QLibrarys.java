package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLibrarys is a Querydsl query type for Librarys
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLibrarys extends EntityPathBase<Librarys> {

    private static final long serialVersionUID = 798182976L;

    public static final QLibrarys librarys = new QLibrarys("librarys");

    public final StringPath address = createString("address");

    public final ListPath<Books, QBooks> booksList = this.<Books, QBooks>createList("booksList", Books.class, QBooks.class, PathInits.DIRECT2);

    public final StringPath closed = createString("closed");

    public final StringPath fax = createString("fax");

    public final StringPath homepage = createString("homepage");

    public final NumberPath<Float> latitude = createNumber("latitude", Float.class);

    public final NumberPath<Long> libcode = createNumber("libcode", Long.class);

    public final StringPath libName = createString("libName");

    public final NumberPath<Float> longitude = createNumber("longitude", Float.class);

    public final StringPath tel = createString("tel");

    public QLibrarys(String variable) {
        super(Librarys.class, forVariable(variable));
    }

    public QLibrarys(Path<? extends Librarys> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLibrarys(PathMetadata metadata) {
        super(Librarys.class, metadata);
    }

}

