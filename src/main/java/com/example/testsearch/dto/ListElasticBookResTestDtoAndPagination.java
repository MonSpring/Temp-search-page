package com.example.testsearch.dto;

import com.example.testsearch.customAnnotation.StopWatchTable;
import com.example.testsearch.dto.Pagination;
import com.example.testsearch.service.ElasticBooksResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListElasticBookResTestDtoAndPagination {

    private List<ElasticBooksResDto> elasticBooksResDtoList;
    private Pagination pagination;
    private String method;
    private Long mills;
    private Long nanos;

    @Builder
    public ListElasticBookResTestDtoAndPagination(List<ElasticBooksResDto> elasticBooksResDtoList, Pagination pagination) {
        this.elasticBooksResDtoList = elasticBooksResDtoList;
        this.pagination = pagination;
    }

    public void updateStopWatch(StopWatchTable stopWatchTable) {
        this.method = stopWatchTable.getMethod();
        this.mills = stopWatchTable.getMills();
        this.nanos = stopWatchTable.getNanos();
    }
}
