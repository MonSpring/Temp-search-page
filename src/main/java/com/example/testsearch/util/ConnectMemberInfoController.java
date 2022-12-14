package com.example.testsearch.util;

import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.dto.Pagination;
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

    @GetMapping("/info")
    public String getLogList(Model model, String startDatetime, String endDatetime,
                             @RequestParam(defaultValue = "1", name = "page") int page,
                             @RequestParam(defaultValue = "10", name = "size") int size) {
        SpreadFrontUtil.spreadFrontCalender(model, startDatetime, endDatetime);
        MemberLoginInfoResControllerDto memberLoginInfoResControllerDto = logMemberAddressService.searchAllMemberAddressLog(startDatetime, endDatetime, page, size);
        model.addAttribute("logdata", memberLoginInfoResControllerDto.getMemberLoginInfoResDtoList());
        model.addAttribute("pagination", memberLoginInfoResControllerDto.getPagination());
        return "logInfoPage";
    }

    @GetMapping("/excel")
    public void excelDownload(HttpServletResponse response,
                              @RequestParam("startDatetime") String startDatetime,
                              @RequestParam("endDatetime") String endDatetime
    ) throws IOException {
        List<MemberLoginInfoResDto> excelList = logMemberAddressService.searchAllMemberAddressLog(startDatetime, endDatetime);
        logMemberAddressService.outputExcel(excelList, response);
    }
}
