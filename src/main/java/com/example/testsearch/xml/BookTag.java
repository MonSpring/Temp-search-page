package com.example.testsearch.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class BookTag {

    @XmlElement(name = "no")
    private String no;

    @XmlElement(name = "bookname")
    private String bookname;

    @XmlElement(name = "authors")
    private String authors;

    @XmlElement(name = "publisher")
    private String publisher;

    @XmlElement(name = "class_no")
    private String class_no;

    @XmlElement(name = "class_nm")
    private String class_nm;

    @XmlElement(name = "publication_year")
    private String publication_year;

    @XmlElement(name = "publication_date")
    private String publication_date;

    @XmlElement(name = "bookImageURL")
    private String bookImageURL;

    @XmlElement(name = "isbn")
    private String isbn;

    @XmlElement(name = "isbn13")
    private String isbn13;

    @XmlElement(name = "description")
    private String description;
}