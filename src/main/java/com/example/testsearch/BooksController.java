package com.example.testsearch;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class BooksController extends HttpServlet {

    private final BookService bookService;

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
                                 @RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestParam("orderBy") String orderBy,
                                 @RequestParam("isAsc")boolean isAsc){
        model.addAttribute("data2", bookService.searchPageable(page, size, orderBy, isAsc));
        return "index";
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
}
