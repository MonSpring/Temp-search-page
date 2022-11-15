package com.example.testsearch.service;

import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.entity.Pagination;
import com.example.testsearch.dto.ListBookResTestDtoAndPagination;
import com.example.testsearch.entity.Books;
import com.example.testsearch.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    // data Jpa pageable 검색
    public List<BookResTestDto> searchPageable(int page, int size, String orderBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<BookResTestDto> bookResTestDtoList = new ArrayList<>();
        Page<Books> booksList = bookRepository.findAll(pageable);

        for (Books books:booksList) {
            BookResTestDto bookResTestDto = new BookResTestDto(books);
            bookResTestDtoList.add(bookResTestDto);
        }
        return bookResTestDtoList;
    }

    // 풀텍스트 인덱스 검색
    public List<BookResTestDto> searchFullText(String searchtext) {

        List<Books> bookList = bookRepository.onlyWordSearchByFullText(searchtext);
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

    @Transactional
    public ListBookResTestDtoAndPagination getSerachBooks(String word, int size, int page, String field, String mode) {

        List<Books> booksList = new ArrayList<>();
        int totalListCnt = 0;

        // 아주 맘에 안듬 한개로 줄일 수 있음
        if (mode.equals("boolean mode")) {
            if (field.equals("title")) {
                booksList = bookRepository.searchTitleFullText(word, size, page);
                totalListCnt = bookRepository.searchTitleFullTextCount(word);
            } else if (field.equals("author")) {
                booksList = bookRepository.searchAuthorFullText(word, size, page);
                totalListCnt = bookRepository.searchAuthorFullTextCount(word);
            } else if (field.equals("publisher")) {
                booksList = bookRepository.searchPublisherFullText(word, size, page);
                totalListCnt = bookRepository.searchPublisherFullTextCount(word);
            } else if (field.equals("isbn")) {
                booksList = bookRepository.searchIsbn(word, size, page);
                totalListCnt = bookRepository.searchIsbnTextCount(word);
            } else {
                throw new RuntimeException("뭐든 골랐어야지 응?");
            }
        } else {
            if (field.equals("title")) {
                booksList = bookRepository.searchNativeTitleFullText(word, size, page);
                totalListCnt = bookRepository.searchNativeTitleFullTextCount(word);
            } else if (field.equals("author")) {
                booksList = bookRepository.searchNativeAuthorFullText(word, size, page);
                totalListCnt = bookRepository.searchNativeAuthorFullTextCount(word);
            } else if (field.equals("publisher")) {
                booksList = bookRepository.searchNativePublisherFullText(word, size, page);
                totalListCnt = bookRepository.searchNativePublisherFullTextCount(word);
            } else if (field.equals("isbn")) {
                booksList = bookRepository.searchIsbn(word, size, page);
                totalListCnt = bookRepository.searchIsbnTextCount(word);
            } else {
                throw new RuntimeException("뭐든 골랐어야지");
            }
        }

        Pagination pagination = new Pagination(totalListCnt, page);

        List<BookResTestDto> bookResTestDtoList = new ArrayList<>();
        for (Books books : booksList) {
            bookResTestDtoList.add(new BookResTestDto(books));
        }

        return ListBookResTestDtoAndPagination.builder()
                .bookResTestDtoList(bookResTestDtoList)
                .pagination(pagination)
                .build();
    }
}
