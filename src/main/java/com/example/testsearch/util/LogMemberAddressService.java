package com.example.testsearch.util;

import com.example.testsearch.dto.Pagination;
import com.example.testsearch.entity.MemberLoginInfo;
import com.example.testsearch.repository.MemberLoginInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogMemberAddressService {

    private final MemberLoginInfoRepository memberLoginInfoRepository;

    public MemberLoginInfoResControllerDto searchAllMemberAddressLog(String startDatetime, String endDatetime, int page, int size) {

        // 타임존 설정 하는 법
        // ZoneId zoneid = ZoneId.of("Asia/Seoul");
        // long startDatetimeFromatMillSecond = startDatetimeFormatTemp.atZone(zoneid).toInstant().toEpochMilli();
        // long endDateTimeFromatMillSecond = endDateTimeFormatTemp.atZone(zoneid).toInstant().toEpochMilli();
        
        // Hot, Warm, Cold 아키텍처 적용 한 달 지나면 데이터 저절로 삭제됨

        // Date Formatting
        String startDatetimeTemp = startDatetime + " 00:00:00";
        String endDatetimeTemp = endDatetime + " 24:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetimeFormatTemp = LocalDateTime.parse(startDatetimeTemp, formatter);
        LocalDateTime endDateTimeFormatTemp = LocalDateTime.parse(endDatetimeTemp, formatter);

        int totaListCnt = memberLoginInfoRepository.countAllByLoginTimeBetween(startDatetimeFormatTemp, endDateTimeFormatTemp);
        log.info("totaListCnt ? : " + totaListCnt);
        Pagination pagination = new Pagination(totaListCnt, page);
        int indexPage = page - 1;
        Pageable pageable = PageRequest.of(indexPage, size);

        Iterable<MemberLoginInfo> memberLoginInfoList = memberLoginInfoRepository.findAllByLoginTimeBetweenOrderByLoginTimeDesc(startDatetimeFormatTemp, endDateTimeFormatTemp, pageable);
        List<MemberLoginInfoResDto> memberLoginInfoResDtoList = new ArrayList<>();

        for (MemberLoginInfo memberLoginInfo:memberLoginInfoList) {
            MemberLoginInfoResDto memberLoginInfoResDto = MemberLoginInfoResDto.builder()
                    .username(memberLoginInfo.getUsername())
                    .memberIp(memberLoginInfo.getMemberIp())
                    .loginTime(memberLoginInfo.getLoginTime())
                    .build();
            memberLoginInfoResDtoList.add(memberLoginInfoResDto);
        }

        return MemberLoginInfoResControllerDto.builder()
                .memberLoginInfoResDtoList(memberLoginInfoResDtoList)
                .pagination(pagination)
                .build();
    }

    public List<MemberLoginInfoResDto> searchAllMemberAddressLog(String startDatetime, String endDatetime) {

        String startDatetimeTemp = startDatetime + " 00:00:00";
        String endDatetimeTemp = endDatetime + " 24:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetimeFormatTemp = LocalDateTime.parse(startDatetimeTemp, formatter);
        LocalDateTime endDateTimeFormatTemp = LocalDateTime.parse(endDatetimeTemp, formatter);

        Iterable<MemberLoginInfo> memberLoginInfoList = memberLoginInfoRepository.findAllByLoginTimeBetweenOrderByLoginTimeDesc(startDatetimeFormatTemp, endDateTimeFormatTemp);
        List<MemberLoginInfoResDto> memberLoginInfoResDtoList = new ArrayList<>();

        for (MemberLoginInfo memberLoginInfo:memberLoginInfoList) {
            MemberLoginInfoResDto memberLoginInfoResDto = MemberLoginInfoResDto.builder()
                    .username(memberLoginInfo.getUsername())
                    .memberIp(memberLoginInfo.getMemberIp())
                    .loginTime(memberLoginInfo.getLoginTime())
                    .build();
            memberLoginInfoResDtoList.add(memberLoginInfoResDto);
        }
        return memberLoginInfoResDtoList;
    }

    public void outputExcel(List<MemberLoginInfoResDto> excelList, HttpServletResponse response) throws IOException {
        //Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("회원");
        cell = row.createCell(1);
        cell.setCellValue("IP");
        cell = row.createCell(2);
        cell.setCellValue("접속시간");

        // Body
        for (MemberLoginInfoResDto memberLoginInfoResDto : excelList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(memberLoginInfoResDto.getUsername());
            cell = row.createCell(1);
            cell.setCellValue(memberLoginInfoResDto.getMemberIp());
            cell = row.createCell(2);
            cell.setCellValue(memberLoginInfoResDto.getLoginTime());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=excel.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        //wb.close
    }
}
