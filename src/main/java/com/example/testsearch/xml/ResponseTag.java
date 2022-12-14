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
public class ResponseTag {

    @XmlElement(name = "request")
    private RequestTag requesttag;

    @XmlElement(name = "detail")
    private Details details;

    @XmlElement(name = "loanInfo")
    private LoanInfo loanInfo;
}
