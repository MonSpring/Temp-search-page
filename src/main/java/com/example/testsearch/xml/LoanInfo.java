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
public class LoanInfo {

    @XmlElement(name = "Total")
    private String Total;

    @XmlElement(name = "regionResult")
    private String regionResult;

    @XmlElement(name = "ageResult")
    private String ageResult;

    @XmlElement(name = "genderResult")
    private String genderResult;

}
