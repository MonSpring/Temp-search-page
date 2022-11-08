package com.example.testsearch;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @LogExecutionTime
    @GetMapping("/index")
    public String getAll(Model model){
        //model.addAttribute("data", bookService.getAll());
        return "index";
    }
}
