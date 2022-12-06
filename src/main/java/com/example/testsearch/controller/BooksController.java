package com.example.testsearch.controller;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.customAnnotation.StopWatchRepository;
import com.example.testsearch.customAnnotation.StopWatchTable;
import com.example.testsearch.dto.BookInfiniteRepoResDto;
import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.dto.ListBookResTestDtoAndPagination;
import com.example.testsearch.dto.Pagination;
import com.example.testsearch.repository.BookRepository;
import com.example.testsearch.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional
public class BooksController extends HttpServlet {

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final StopWatchRepository stopWatchRepository;

    private int callCount = 0;

    private int lastIdChange = 0;

    // 기본 페이지
    @GetMapping("/index")
    public String main() {
        return "redirect:/user/login";
    }

    // data Jpa 페이저블 사용한 기존 페이지네이션 검색
    @LogExecutionTime
    @GetMapping("/pageable")
    public String searchPageable(Model model,
                                 @RequestParam(defaultValue = "1", name = "page") int page,
                                 @RequestParam(defaultValue = "10", name = "size") int size,
                                 @RequestParam(defaultValue = "id", name = "orderBy") String orderBy,
                                 @RequestParam(defaultValue = "false", name = "isAsc") boolean isAsc) {
        // 총 게시물 수
        int totalListCnt = (int) bookRepository.count();

        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        model.addAttribute("data2", bookService.searchPageable(page, size, orderBy, isAsc));
        model.addAttribute("pagination", pagination);

        return "pageable";
    }

    // JPQL 검색
    @GetMapping("/searchJpql")
    public String jpqlSearch(Model model,
                             @RequestParam String word,
                             @RequestParam(defaultValue = "1", name = "page") int page,
                             @RequestParam(defaultValue = "10", name = "size") int size,
                             @RequestParam String field,
                             @RequestParam String mode) {

        log.info(field);
        log.info(mode);

        ListBookResTestDtoAndPagination listBookResTestDtoAndPagination = bookService.getSerachBooks(word, size, page, field, mode);

        // 검색 리스트 가져오는 용도
        model.addAttribute("data5", listBookResTestDtoAndPagination.getBookResTestDtoList());
        // page 버튼 뿌려주는 용도
        model.addAttribute("pagination", listBookResTestDtoAndPagination.getPagination());
        model.addAttribute("word", word);
        model.addAttribute("field", field);
        model.addAttribute("mode", mode);

        // 메소드 검색 시간 체크 프론트에 뿌려주는 용도
        StopWatchTable stopWatchTable = stopWatchRepository.findTopByOrderByIdDesc();
        listBookResTestDtoAndPagination.updateStopWatch(stopWatchTable);
        model.addAttribute("method", listBookResTestDtoAndPagination.getMethod());
        model.addAttribute("mills", listBookResTestDtoAndPagination.getMills());
        model.addAttribute("nanos", listBookResTestDtoAndPagination.getNanos());

        return "searchPage";
    }

    @GetMapping("/querydsl")
    public String querydsl(Model model,
                           @RequestParam("word") String word,
                           @RequestParam("mode") String mode,
                           @RequestParam String field,
                           @RequestParam(defaultValue = "1", name = "page") int page,
                           @RequestParam(defaultValue = "10", name = "size") int size) {

        ListBookResTestDtoAndPagination listBookResTestDtoAndPagination = bookService.searchFullTextQueryDsl(word, mode, page, size, field);

        model.addAttribute("data6", listBookResTestDtoAndPagination.getBookResTestDtoList());
        model.addAttribute("pagination", listBookResTestDtoAndPagination.getPagination());
        model.addAttribute("word", word);
        model.addAttribute("field", field);
        model.addAttribute("mode", mode);

        // 메소드 검색 시간 체크 프론트에 뿌려주는 용도
        StopWatchTable stopWatchTable = stopWatchRepository.findTopByOrderByIdDesc();
        listBookResTestDtoAndPagination.updateStopWatch(stopWatchTable);
        model.addAttribute("method", listBookResTestDtoAndPagination.getMethod());
        model.addAttribute("mills", listBookResTestDtoAndPagination.getMills());
        model.addAttribute("nanos", listBookResTestDtoAndPagination.getNanos());

        return "querydsl";
    }

    /**
     * querydsl excel
     */
    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response,
                              @RequestParam("word") String word,
                              @RequestParam("mode") String mode,
                              @RequestParam String field
    ) throws IOException {

        List<BookResTestDto> excelList = bookService.Excel(word, mode, field);
        //Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("책이름");
        cell = row.createCell(2);
        cell.setCellValue("작가");
        cell = row.createCell(3);
        cell.setCellValue("출판사");
        cell = row.createCell(4);
        cell.setCellValue("권수");
        cell = row.createCell(5);
        cell.setCellValue("isbn");

        // Body
        for (int i = 0; i < excelList.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(excelList.get(i).getTitle());
            cell = row.createCell(2);
            cell.setCellValue(excelList.get(i).getAuthor());
            cell = row.createCell(3);
            cell.setCellValue(excelList.get(i).getBookCount());
            cell = row.createCell(4);
            cell.setCellValue(excelList.get(i).getBookCount());
            cell = row.createCell(5);
            cell.setCellValue(excelList.get(i).getIsbn());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=excel.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        //wb.close
    }

    /**
     * jpql excel
     */
    @GetMapping("/jpql/excel/download")
    public void jpqlExcelDownload(HttpServletResponse response,
                                  @RequestParam("word") String word,
                                  @RequestParam("mode") String mode,
                                  @RequestParam String field
    ) throws IOException {

        List<BookResTestDto> excelList = bookService.JpqlExcel(word, mode, field);
        //Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("책이름");
        cell = row.createCell(2);
        cell.setCellValue("작가");
        cell = row.createCell(3);
        cell.setCellValue("출판사");
        cell = row.createCell(4);
        cell.setCellValue("권수");
        cell = row.createCell(5);
        cell.setCellValue("isbn");

        // Body
        for (int i = 0; i < excelList.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(excelList.get(i).getTitle());
            cell = row.createCell(2);
            cell.setCellValue(excelList.get(i).getAuthor());
            cell = row.createCell(3);
            cell.setCellValue(excelList.get(i).getBookCount());
            cell = row.createCell(4);
            cell.setCellValue(excelList.get(i).getBookCount());
            cell = row.createCell(5);
            cell.setCellValue(excelList.get(i).getIsbn());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=excel.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        //wb.close
    }

    @GetMapping("/books/{id}/detail/{isbn}")
    public String detailModal(Model model,
                              @PathVariable(name = "id") Long bookId,
                              @PathVariable(name = "isbn") Long isbn){

        model.addAttribute("data", bookService.searchDetail(bookId, isbn));

        return "bookDetail";
    }

    // 무한 스크롤 서치 페이지
    @GetMapping("/search")
    public String search() {
        callCount = 0;
        lastIdChange = bookService.searchInfinityCount();

        return "search";
    }

    @PostMapping("/infinitescroll")
    public ResponseEntity getInfiniteBooksList() {
        List<BookInfiniteRepoResDto> bookInfiniteResDtoList;
        int limitSize = 30;
        if (callCount == 0) {
            bookInfiniteResDtoList = bookService.getInfiniteBooksList(lastIdChange, limitSize);
            callCount += 1;
        } else {
            lastIdChange -= limitSize;
            bookInfiniteResDtoList = bookService.getInfiniteBooksList(lastIdChange, limitSize);
            callCount += 1;
        }
        return ResponseEntity.ok().body(bookInfiniteResDtoList);
    }
}
