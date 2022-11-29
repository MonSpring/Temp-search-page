package com.example.testsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetails {

    private static final String PROCEDURE_PARAM = "AAA";

    @Id
    @Column(name = "book_detail_id")
    @GeneratedValue(generator = "a")
    @GenericGenerator(name = "a" , strategy = "increment")
    private Long id;

    @Lob
    @Column
    private String description;

    @Column
    private String thumbnail;

    @Column
    private Long isbn;

}
