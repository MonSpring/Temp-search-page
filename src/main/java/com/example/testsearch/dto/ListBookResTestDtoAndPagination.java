package com.example.testsearch.dto;

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
