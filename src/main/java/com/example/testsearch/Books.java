package com.example.testsearch;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Books {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String publisher;
    @Column
    private String publication_year;
    @Column
    private Long isbn;
    @Column
    private String vol;
//    @Column
//    private int numberOfBooks;
//    @Column
//    private int numberOfLoans;
    @Column
    private Date registrationDate;
}
