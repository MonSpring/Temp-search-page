package com.example.testsearch.customAnnotation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStopWatchTable is a Querydsl query type for StopWatchTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStopWatchTable extends EntityPathBase<StopWatchTable> {

    private static final long serialVersionUID = 1853559692L;

    public static final QStopWatchTable stopWatchTable = new QStopWatchTable("stopWatchTable");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath method = createString("method");

    public final NumberPath<Long> mills = createNumber("mills", Long.class);

    public final NumberPath<Long> nanos = createNumber("nanos", Long.class);

    public QStopWatchTable(String variable) {
        super(StopWatchTable.class, forVariable(variable));
    }

    public QStopWatchTable(Path<? extends StopWatchTable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStopWatchTable(PathMetadata metadata) {
        super(StopWatchTable.class, metadata);
    }

}

