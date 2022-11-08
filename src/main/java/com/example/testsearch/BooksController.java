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

    @LogExecutionTime
    @GetMapping("/index")
    public String getAll(Model model){
        model.addAttribute("data", bookService.getAll());
        return "index";
    }


    @LogExecutionTime
    @GetMapping("/index")
    public String searchFullText(Model model, @RequestParam("searchText") String searchText){
        model.addAttribute("data", bookService.searchFullText(searchText));
        return "index";
    }
}
