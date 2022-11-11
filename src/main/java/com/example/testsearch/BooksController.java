package com.example.testsearch;

import com.example.testsearch.customAnnotation.ListBookResTestDtoAndPagination;
import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

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
                             @RequestParam(defaultValue = "10", name = "size") int size){

        ListBookResTestDtoAndPagination listBookResTestDtoAndPagination = bookService.getSerachBooks(word, size, page);
        // 검색 리스트 가져오는 용도
        model.addAttribute("data5", listBookResTestDtoAndPagination.getBookResTestDtoList());
        // page 버튼 뿌려주는 용도
        model.addAttribute("pagination", listBookResTestDtoAndPagination.getPagination());

        return "searchPage";
    }

}
