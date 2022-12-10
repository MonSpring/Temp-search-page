package com.example.testsearch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;

@Getter
@Setter
@Document(indexName = "elasticbooks")
@NoArgsConstructor
public class ElasticBooks {

    @Id
    private String id;

    @Column
    private Long book_id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String publisher;

    @Column(name = "publication_year")
    private String publicationYear;

    @Column
    private Long isbn;

    @Column(name = "book_count")
    private String bookCount;

    @Column(name = "lend_out_book_count")
    private Long lendOutBookCount;

    @Column(name = "reg_date")
    private String regDate;

    @Column
    private Long libcode;

    @Column(name = "title.Keyword")
    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Column(name = "author.Keyword")
    @Field(type = FieldType.Keyword)
    private String authorKeyword;

    @Column(name = "publisher.Keyword")
    @Field(type = FieldType.Keyword)
    private String publisherKeyword;

    @Column(name = "publication_year.Keyword")
    @Field(type = FieldType.Keyword)
    private String publicationYearKeyword;

    @Column(name = "book_count.Keyword")
    @Field(type = FieldType.Keyword)
    private String bookCountKeyword;

    @Column(name = "reg_date.Keyword")
    @Field(type = FieldType.Keyword)
    private String regDateKeyword;

    @Builder
    public ElasticBooks(String id, Long book_id, String title, String author, String publisher, String publicationYear, Long isbn, String bookCount, Long lendOutBookCount, String regDate, Long libcode) {
        this.id = id;
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.bookCount = bookCount;
        this.lendOutBookCount = lendOutBookCount;
        this.regDate = regDate;
        this.libcode = libcode;
    }
}

