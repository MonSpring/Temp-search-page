package com.example.testsearch.customAnnotation;

import com.example.testsearch.BookResTestDto;
import com.example.testsearch.Pagination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListBookResTestDtoAndPagination {

    private List<BookResTestDto> bookResTestDtoList;
    private Pagination pagination;

    @Builder
    public ListBookResTestDtoAndPagination(List<BookResTestDto> bookResTestDtoList, Pagination pagination) {
        this.bookResTestDtoList = bookResTestDtoList;
        this.pagination = pagination;
    }
}
