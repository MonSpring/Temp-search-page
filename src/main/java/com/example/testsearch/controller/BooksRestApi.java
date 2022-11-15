package com.example.testsearch.controller;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BooksRestApi {

    private final BookService bookService;

    @LogExecutionTime
    @PostMapping("/books/{id}")
    public BookResTestDto getBooks(@PathVariable long id) {
        return bookService.getBooks(id);
    }
}
