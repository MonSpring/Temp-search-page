package com.example.testsearch.service;

import com.example.testsearch.dto.BookResTestDto;
import com.example.testsearch.dto.Pagination;
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

        // Total Rows 가져오는 SEQ
        if (mode.equals("boolean mode")) {
            if (field.equals("title")) {
                totalListCnt = bookRepository.searchTitleFullTextCount(word);
            } else if (field.equals("author")) {
                totalListCnt = bookRepository.searchAuthorFullTextCount(word);
            } else if (field.equals("publisher")) {
                totalListCnt = bookRepository.searchPublisherFullTextCount(word);
            } else {
                totalListCnt = bookRepository.searchIsbnTextCount(word);
            }
        } else {
            if (field.equals("title")) {
                totalListCnt = bookRepository.searchNativeTitleFullTextCount(word);
            } else if (field.equals("author")) {
                totalListCnt = bookRepository.searchNativeAuthorFullTextCount(word);
            } else if (field.equals("publisher")) {
                totalListCnt = bookRepository.searchNativePublisherFullTextCount(word);
            } else {
                totalListCnt = bookRepository.searchIsbnTextCount(word);
            }
        }

        Pagination pagination = new Pagination(totalListCnt, page);
        int pageOffset = pagination.getStartIndex();
        log.info("total rows : " + totalListCnt);

        // List 가져오는 SEQ
        if (mode.equals("boolean mode")) {
            if (field.equals("title")) {
                booksList = bookRepository.searchTitleFullText(word, size, pageOffset);
            } else if (field.equals("author")) {
                booksList = bookRepository.searchAuthorFullText(word, size, pageOffset);
            } else if (field.equals("publisher")) {
                booksList = bookRepository.searchPublisherFullText(word, size, pageOffset);
            } else {
                booksList = bookRepository.searchIsbn(word, size, pageOffset);
            }
        } else {
            if (field.equals("title")) {
                booksList = bookRepository.searchNativeTitleFullText(word, size, pageOffset);
            } else if (field.equals("author")) {
                booksList = bookRepository.searchNativeAuthorFullText(word, size, pageOffset);
            } else if (field.equals("publisher")) {
                booksList = bookRepository.searchNativePublisherFullText(word, size, pageOffset);
            } else {
                booksList = bookRepository.searchIsbn(word, size, pageOffset);
            }
        }

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
