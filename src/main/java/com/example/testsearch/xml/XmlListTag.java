package com.example.testsearch.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)  // setter는 제외하고 필드 값만 바인딩
@XmlRootElement(name = "response") // 최상위 태그 명시
@Getter
@Setter
@ToString
public class XmlListTag {

    @XmlElement(name = "response")
    private ResponseTag responseTags;
}