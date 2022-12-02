package com.example.testsearch.util;

import com.example.testsearch.dto.BookResTestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/log")
@RequiredArgsConstructor
public class ConnectMemberInfoController {

    private final LogMemberAddressService logMemberAddressService;

    @GetMapping
    public String logInfoPage(Model model) {
        SpreadFrontUtil.spreadFrontCalender(model);
        return "logInfoPage";
    }

    @PostMapping("/info")
    public String getLogList(Model model, String startDatetime, String endDatetime) {
        SpreadFrontUtil.spreadFrontCalender(model, startDatetime, endDatetime);
        model.addAttribute("logdata", logMemberAddressService.searchAllMemberAddressLog(startDatetime, endDatetime));
        return "logInfoPage";
    }

    @GetMapping("/excel")
    public void excelDownload(HttpServletResponse response,
                              @RequestParam("startDatetime") String startDatetime,
                              @RequestParam("endDatetime") String endDatetime
    ) throws IOException {

        List<MemberLoginInfoResDto> excelList = logMemberAddressService.searchAllMemberAddressLog(startDatetime, endDatetime);
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
