package com.example.testsearch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter @Setter
@Document(indexName = "memberlogininfo")
public class MemberLoginInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String username;
    private String memberIp;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime loginTime;

    @Builder
    public MemberLoginInfo(String username, String memberIp, LocalDateTime loginTime) {
        this.username = username;
        this.memberIp = memberIp;
        this.loginTime = loginTime;
    }
}
