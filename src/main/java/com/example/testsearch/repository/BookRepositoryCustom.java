package com.example.testsearch.repository;

import com.example.testsearch.dto.BookResTestDto;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepositoryCustom{
    List<BookResTestDto> searchByFullTextBooleanTest(@Param("word") String word, String mode, int page, int size, String field);
    int searchByFullTextBooleanCount(@Param("word") String word,String mode, String field);

    int searchByIsbnCountQuery(@Param("word") String word,String mode, String field);

    List<BookResTestDto> forExcelQuery(String word, String mode, String field);
}
