package com.example.testsearch.elasticsearch;

import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import javax.persistence.Id;

@Setter
@Document(indexName = "blog")
public class Blog {
    @Id
    private String id;
    private String title;
    private String content;
}
