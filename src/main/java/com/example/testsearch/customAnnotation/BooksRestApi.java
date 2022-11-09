package com.example.testsearch.customAnnotation;

import com.example.testsearch.BookResTestDto;
import com.example.testsearch.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
