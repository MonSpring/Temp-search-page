package com.example.testsearch.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberTest is a Querydsl query type for MemberTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberTest extends EntityPathBase<MemberTest> {

    private static final long serialVersionUID = 1782323060L;

    public static final QMemberTest memberTest = new QMemberTest("memberTest");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QMemberTest(String variable) {
        super(MemberTest.class, forVariable(variable));
    }

    public QMemberTest(Path<? extends MemberTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberTest(PathMetadata metadata) {
        super(MemberTest.class, metadata);
    }

}

