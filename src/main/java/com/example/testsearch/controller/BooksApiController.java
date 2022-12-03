package com.example.testsearch.controller;

import com.example.testsearch.dto.BookInfiniteResDto;
import com.example.testsearch.service.BookService;
import com.example.testsearch.service.BooksApiService;
import com.example.testsearch.customAnnotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BooksApiController {

    private final BooksApiService booksApiService;

    @ResponseBody
    @LogExecutionTime
    @GetMapping("/api/search")
    public void getItems(@RequestParam String isbn) throws JAXBException {
        booksApiService.getItems(isbn);
    }

}
