package com.example.testsearch.repository;

import com.example.testsearch.dto.BookResTestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface BookRepositoryCustom{
    Page<BookResTestDto> searchByFullTextBooleanTest(@Param("word") String word,String mode, int page, int size, Pageable pageable, String field);
}
