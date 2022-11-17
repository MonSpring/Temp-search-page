package com.example.testsearch.controller;

import com.example.testsearch.repository.BookRepository;
import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.service.BookService;
import com.example.testsearch.dto.Pagination;
import com.example.testsearch.dto.ListBookResTestDtoAndPagination;
import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.transaction.Transactional;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional
public class BooksController extends HttpServlet {

    private final BookService bookService;

    private final BookRepository bookRepository;


//    @GetMapping("/index")
//    public String getToken(HttpServletRequest httpServletRequest) {
//        String AssorizationToken = httpServletRequest.getCookies("Assorization");
//        return AssorizationToken;
//    }

    // 1630만개 끌어오기
    @LogExecutionTime
    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("data", bookService.getAll());
        return "index";
    }

    // data Jpa 페이저블 사용한 기존 페이지네이션 검색
    @LogExecutionTime
    @GetMapping("/pageable")
    public String searchPageable(Model model,
                                 @RequestParam(defaultValue = "1", name = "page") int page,
                                 @RequestParam(defaultValue = "10", name = "size") int size,
                                 @RequestParam(defaultValue = "id", name = "orderBy") String orderBy,
                                 @RequestParam(defaultValue = "false", name = "isAsc")boolean isAsc){
        // 총 게시물 수
        int totalListCnt = (int) bookRepository.count();

        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
/*
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();*/

        /*List<Board> boardList = boardRepository.findListPaging(startIndex, pageSize);*/

        model.addAttribute("data2", bookService.searchPageable(page, size, orderBy, isAsc));
        model.addAttribute("pagination", pagination);
        return "pageable";
    }

//    @LogExecutionTime
//    @PostMapping("/jpql")
//    public String searchSqlPageable(Model model,
//                                 @RequestParam("page") int page,
//                                 @RequestParam("offset") int offset,
//                                 @RequestParam("limit") int limit){
//        model.addAttribute("data3", bookService.searchSqlPageable(page, offset, limit));
//        return "index";
//    }

    // 풀텍스트 인덱스 검색
    @LogExecutionTime
    @GetMapping("/search")
    public String searchFullText(Model model, @RequestParam("searchText") String searchText){
        model.addAttribute("data4", bookService.searchFullText(searchText));
        return "index";
    }

    // jpal 검색
    @LogExecutionTime
    @GetMapping("/searchJpql")
    public String jpqlSearch(Model model,
                             @RequestParam String word,
                             @RequestParam(defaultValue = "1", name = "page") int page,
                             @RequestParam(defaultValue = "10", name = "size") int size,
                             @RequestParam String field,
                             @RequestParam String mode){

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

        return "searchPage";
    }

    @LogExecutionTime
    @GetMapping("/querydsl")
    public String querydsl(Model model,
                                         @RequestParam("word") String word,
                                         @RequestParam("mode") String mode,
                                         @RequestParam String field,
                                         @RequestParam(defaultValue = "1", name = "page") int page,
                                         @RequestParam(defaultValue = "10", name = "size") int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<BookResTestDto> bookResTestDtos = bookRepository.searchByFullTextBooleanTest(word, mode, page, size, pageable, field);
        model.addAttribute("data6", bookResTestDtos);
        Pagination pagination = new Pagination((int) bookResTestDtos.getTotalElements(), page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("word", word);
        model.addAttribute("field", field);
        model.addAttribute("mode", mode);

        return "querydsl";
    }

}
