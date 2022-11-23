package com.example.testsearch;

import com.example.testsearch.elasticsearch.Blog;
import com.example.testsearch.elasticsearch.BlogEsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TestElasticSearch {

    @Resource
    BlogEsRepository blogEsRepository;

    @Test
    void test(){
        Blog blog = new Blog();
        blog.setId("2");
        blog.setContent("이창욱 바보");
        blog.setTitle("제목입니다.");
        blogEsRepository.save(blog);
    }

}
