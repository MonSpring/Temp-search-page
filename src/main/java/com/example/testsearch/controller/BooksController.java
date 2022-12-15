package com.example.testsearch.controller;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.customAnnotation.StopWatchRepository;
import com.example.testsearch.customAnnotation.StopWatchTable;
import com.example.testsearch.dto.*;
import com.example.testsearch.repository.BookRepository;
import com.example.testsearch.repository.LibrarysRepository;
import com.example.testsearch.service.BookService;
import com.example.testsearch.service.NotificationService;
import com.example.testsearch.dto.ElasticBooksResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional
public class BooksController extends HttpServlet {

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final StopWatchRepository stopWatchRepository;
    private final LibrarysRepository librarysRepository;

    private final NotificationService notificationService;

    private final StringRedisTemplate stringRedisTemplate;


    private int callCount = 0;

    private int lastIdChange = 0;

    private static int countId = 0;

    List<Long> memberIdList = new ArrayList<>();

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

    // nativeQuery 검색
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
            cell.setCellValue(excelList.get(i).getPublisher());
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
            cell.setCellValue(excelList.get(i).getPublisher());
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
                              @PathVariable(name="id")Long bookId,
                              @PathVariable(name="isbn")Long isbn,
                              HttpServletRequest request){

        String username = null;

        model.addAttribute("countPost", -1);

        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            }

            if(cookie.getName().equals("countPost")){
                model.addAttribute("countPost", Integer.parseInt(cookie.getValue()));
            }
            if(cookie.getName().equals("event")){
                model.addAttribute("event", cookie.getValue());
            }

        }

        model.addAttribute("data", bookService.searchDetail(bookId, isbn, username));

        return "bookDetail";
    }

    @PostMapping("/books/{id}/rental")
    public String rentalBooks(Model model,
                              @PathVariable(name="id")Long bookId,
                              HttpServletRequest request,
                              HttpServletResponse response){

        String username = null;

        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            }

            if(cookie.getName().equals("countPost")){
                log.info(cookie.getValue());
            }
        }
        String successMessage = bookService.rentalBook(bookId, username);

        Long isbn = bookService.findIsbn(bookId);

        return "redirect:/books/" + bookId + "/detail/" + isbn;
    }

    @PostMapping("/books/{id}/return")
    public String returnBooks(Model model,
                              @PathVariable(name="id")Long bookId,
                              HttpServletRequest request,
                              HttpServletResponse response){

        String username = null;

        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            }

            if(cookie.getName().equals("countPost")){
                log.info(cookie.getValue());
            }
        }

        String successMessage = bookService.returnBook(bookId, username);

        Long isbn = bookService.findIsbn(bookId);

        return "redirect:/books/" + bookId + "/detail/" + isbn;
    }

    public synchronized long minusBookCount(long bookId, long bookCount) {

        bookCount = bookCount - countId++;

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

        while (stringStringValueOperations.get(bookId + String.valueOf(bookCount)) != null) {
            bookCount--;
            if(bookCount < 1)
                break;
        }

        long LIMIT_TIME = 3000;
        stringStringValueOperations.set(bookId + String.valueOf(bookCount), String.valueOf(bookCount), LIMIT_TIME, TimeUnit.MILLISECONDS);

        return bookCount;
    }

    public boolean isValidationRentalTest(Long memberId) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

        if(stringStringValueOperations.get(String.valueOf(memberId)) != null) {
            return false;
        }

        // 1초
        long LIMIT_TIME = 1000;
        stringStringValueOperations.set(String.valueOf(memberId), String.valueOf(memberId), LIMIT_TIME, TimeUnit.MILLISECONDS);

        return true;
    }

    @PostMapping("/books/{id}/rental/JMeterTest/{member_id}")
    public String rentalBooksJMeterTest(@PathVariable(name="id")Long bookId,
                                        @PathVariable(name="member_id")Long memberId,
                                        HttpServletResponse response) {

        long bookCount = bookService.countRentalBookTest(bookId);

        long quantityOfBook = minusBookCount(bookId, bookCount);

        if(isValidationRentalTest(memberId)) {
            if (quantityOfBook > 0) {
                String successMessage = bookService.rentalBookTest(bookId, memberId);

                // username 쿠키 1시간
                Cookie cookie = new Cookie("event", successMessage);
                cookie.setMaxAge(3600);
                cookie.setPath("/");
                response.addCookie(cookie);

                if (cookie.getName().equals("event")) {
                    log.info(cookie.getValue());
                }
            } else {
                Cookie cookie = new Cookie("event", "수량부족");
                cookie.setMaxAge(3600);
                cookie.setPath("/");
                response.addCookie(cookie);

                if (cookie.getName().equals("event")) {
                    log.info(cookie.getValue());
                }
            }
        }

        return "redirect:/search";

    }

    // 무한 스크롤 서치 페이지
    @GetMapping("/search")
    public String search(HttpServletRequest request, Model model) {
        callCount = 0;
        lastIdChange = bookService.searchInfinityCount();

        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("username")){
                model.addAttribute("username", cookie.getValue());
            }
        }

        List<LibraryResDtoV2> findLibrary = librarysRepository.findByLibrary();
        model.addAttribute("library",findLibrary);
        model.addAttribute("msg", notificationService.getMessage());

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

    // ElasticSearch 검색
    @GetMapping("/elasticsearch")
    public String elasticSearch(Model model,
                                                  @RequestParam String word,
                                                  @RequestParam(defaultValue = "1", name = "page") int page,
                                                  @RequestParam(defaultValue = "10", name = "size") int size,
                                                  @RequestParam String field,
                                                  @RequestParam(defaultValue = "text", name = "mode") String mode) {

        ListElasticBookResTestDtoAndPagination listElasticBookResTestDtoAndPagination = bookService.getElasticBooksSearch(word, size, page, field, mode);

        // 검색 리스트 가져오는 용도
        model.addAttribute("data7", listElasticBookResTestDtoAndPagination.getElasticBooksResDtoList());

        // page 버튼 뿌려주는 용도
        model.addAttribute("pagination", listElasticBookResTestDtoAndPagination.getPagination());
        model.addAttribute("word", word);
        model.addAttribute("field", field);
        model.addAttribute("mode", mode);

        // 메소드 검색 시간 체크 프론트에 뿌려주는 용도
        StopWatchTable stopWatchTable = stopWatchRepository.findTopByOrderByIdDesc();
        listElasticBookResTestDtoAndPagination.updateStopWatch(stopWatchTable);
        model.addAttribute("method", listElasticBookResTestDtoAndPagination.getMethod());
        model.addAttribute("mills", listElasticBookResTestDtoAndPagination.getMills());
        model.addAttribute("nanos", listElasticBookResTestDtoAndPagination.getNanos());

        return "elasticsearch";
    }

    @GetMapping("/elasticsearch/excel")
    public void searchElasticSearchBooksListOutputExcel(HttpServletResponse res,
                                                        @RequestParam String word,
                                                        @RequestParam(defaultValue = "text", name = "mode") String mode,
                                                        @RequestParam String field) throws IOException {
        List<ElasticBooksResDto> elasticBooksResDtoList = bookService.searchElasticForExcel(word, mode, field);;
        bookService.outputExcelForElastic(elasticBooksResDtoList, res);
    }

    @GetMapping("/library")
    public String getLibrary() {
        return "libraryPage";
    }

    @LogExecutionTime
    @GetMapping("/library/info")
    public String searchLibraryV2(
            Model model,
            @RequestParam(name ="libcode") Long  libcode,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {

        List<LibraryResDtoV2> findLibrary = librarysRepository.findByLibrary();
        model.addAttribute("library",findLibrary);

        ListBookResTestDtoAndPagination listBookResTestDtoAndPagination = bookService.searchLibraryV2(libcode, size, page);
        // 검색 리스트 가져오는 용도
        model.addAttribute("data10", listBookResTestDtoAndPagination.getBookResTestDtoList());
        // page 버튼 뿌려주는 용도
        model.addAttribute("pagination", listBookResTestDtoAndPagination.getPagination());
        log.info("listBookResTestDtoAndPagination.getPagination(): " + listBookResTestDtoAndPagination.getPagination().getStartIndex());
        model.addAttribute("libcode", libcode);

        // 메소드 검색 시간 체크 프론트에 뿌려주는 용도
        StopWatchTable stopWatchTable = stopWatchRepository.findTopByOrderByIdDesc();
        listBookResTestDtoAndPagination.updateStopWatch(stopWatchTable);
        model.addAttribute("method", listBookResTestDtoAndPagination.getMethod());
        model.addAttribute("mills", listBookResTestDtoAndPagination.getMills());
        model.addAttribute("nanos", listBookResTestDtoAndPagination.getNanos());

        return "libraryPage";
    }

    /**
     * jpql excel
     */
    @GetMapping("/library/excel/download")
    public void jpqlExcelDownload(HttpServletResponse response,
                                  @RequestParam("libcode") Long libcode
    ) throws IOException {

        List<LibRepoResDto> excelList = bookService.libraryExcel(libcode);
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
        cell = row.createCell(6);
        cell.setCellValue("도서관이름");

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
            cell.setCellValue(excelList.get(i).getPublisher());
            cell = row.createCell(4);
            cell.setCellValue(excelList.get(i).getBook_count());
            cell = row.createCell(5);
            cell.setCellValue(excelList.get(i).getIsbn());
            cell = row.createCell(6);
            cell.setCellValue(excelList.get(i).getLib_name());
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
