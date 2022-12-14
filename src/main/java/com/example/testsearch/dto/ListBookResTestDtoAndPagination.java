package com.example.testsearch.dto;

import com.example.testsearch.customAnnotation.StopWatchTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Getter @Setter
public class ListBookResTestDtoAndPagination {

    private List<?> bookResTestDtoList;
    private Pagination pagination;
    private String method;
    private Long mills;
    private Long nanos;

    @Builder
    public ListBookResTestDtoAndPagination(List<?> bookResTestDtoList, Pagination pagination) {
        this.bookResTestDtoList = bookResTestDtoList;
        this.pagination = pagination;
    }

    public void updateStopWatch(StopWatchTable stopWatchTable) {
        this.method = stopWatchTable.getMethod();
        this.mills = stopWatchTable.getMills();
        this.nanos = stopWatchTable.getNanos();
    }
}
