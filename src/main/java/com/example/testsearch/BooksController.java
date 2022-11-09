package com.example.testsearch;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    // 1630만개 끌어오기
    @LogExecutionTime
    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("data", bookService.getAll());
        return "index";
    }

    // 풀텍스트 인덱스 검색
    @LogExecutionTime
    @GetMapping("/search")
    public String searchFullText(Model model, @RequestParam("searchText") String searchText){
        model.addAttribute("data", bookService.searchFullText(searchText));
        return "index";
    }
}
