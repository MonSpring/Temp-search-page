package com.example.testsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 1630만개 끌고오기
    public List<BookResTestDto> getAll() {

        List<Books> bookList = bookRepository.findAll();
        List<BookResTestDto> bookResTestDtos = new ArrayList<>();
        for (Books books : bookList) {
            bookResTestDtos.add(new BookResTestDto(books));
        }

        return bookResTestDtos;
    }

    // 풀텍스트 인덱스 검색
    public List<BookResTestDto> searchFullText(String searchtext) {

        List<Books> bookList = bookRepository.searchByFullText(searchtext);
        List<BookResTestDto> bookResTestDtos = new ArrayList<>();
        for (Books books : bookList) {
            bookResTestDtos.add(new BookResTestDto(books));
        }

        return bookResTestDtos;
    }

    // 한개 가져오기
    public BookResTestDto getBooks(long id) {
        Books books = bookRepository.findById(id).orElseThrow(()->new RuntimeException("책을 찾을 수 없습니다"));
        return new BookResTestDto(books);
    }
}
