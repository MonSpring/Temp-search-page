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
public class RequestTag {

    @XmlElement(name = "isbn13")
    private String isbn13;

    @XmlElement(name = "loaninfoYN")
    private String loaninfoYN;
}
