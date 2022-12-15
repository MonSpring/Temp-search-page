package com.example.testsearch.util;

import com.example.testsearch.dto.Pagination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MemberLoginInfoResControllerDto {

    private Pagination pagination;
    private List<MemberLoginInfoResDto> memberLoginInfoResDtoList;

    @Builder
    public MemberLoginInfoResControllerDto(Pagination pagination, List<MemberLoginInfoResDto> memberLoginInfoResDtoList) {
        this.pagination = pagination;
        this.memberLoginInfoResDtoList = memberLoginInfoResDtoList;
    }
}
