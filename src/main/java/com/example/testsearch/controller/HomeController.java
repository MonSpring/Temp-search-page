package com.example.testsearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    // 기본 페이지
    @GetMapping("/")
    public String home() {
        return "login";
    }

    // 서치 페이지
    @GetMapping("/search")
    public String search() {
        return "search";
    }
}
