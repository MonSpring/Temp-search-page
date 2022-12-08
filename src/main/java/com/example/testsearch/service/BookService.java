package com.example.testsearch.service;

import com.example.testsearch.controller.SseController;
import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.dto.*;
import com.example.testsearch.entity.BookDetails;
import com.example.testsearch.entity.BookRentals;
import com.example.testsearch.entity.Books;
import com.example.testsearch.entity.Member;
import com.example.testsearch.repository.BookDetailRepository;
import com.example.testsearch.repository.BookRentalRepository;
import com.example.testsearch.repository.BookRepository;
import com.example.testsearch.repository.MemberRepository;
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

    private final BookDetailRepository bookDetailRepository;

    private final BookRentalRepository bookRentalRepository;

    private final MemberRepository memberRepository;

    private final SseController sseController;

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

    @LogExecutionTime
    @Transactional
    public ListBookResTestDtoAndPagination getSerachBooks(String word, int size, int page, String field, String mode) {

        List<Books> booksList = new ArrayList<>();
        int totalListCnt = 0;

        // Total Rows 가져오는 SEQ
        if (mode.equals("boolean mode")) {
            switch (field) {
                case "title":
                    totalListCnt = bookRepository.searchTitleFullTextCount(word);
                    break;
                case "author":
                    totalListCnt = bookRepository.searchAuthorFullTextCount(word);
                    break;
                case "publisher":
                    totalListCnt = bookRepository.searchPublisherFullTextCount(word);
                    break;
                default:
                    totalListCnt = bookRepository.searchIsbnTextCount(word);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    totalListCnt = bookRepository.searchNativeTitleFullTextCount(word);
                    break;
                case "author":
                    totalListCnt = bookRepository.searchNativeAuthorFullTextCount(word);
                    break;
                case "publisher":
                    totalListCnt = bookRepository.searchNativePublisherFullTextCount(word);
                    break;
                default:
                    totalListCnt = bookRepository.searchIsbnTextCount(word);
                    break;
            }
        }

        Pagination pagination = new Pagination(totalListCnt, page);
        int pageOffset = pagination.getStartIndex();
        log.info("total rows : " + totalListCnt);

        // List 가져오는 SEQ
        if (mode.equals("boolean mode")) {
            switch (field) {
                case "title":
                    booksList = bookRepository.searchTitleFullText(word, size, pageOffset);
                    break;
                case "author":
                    booksList = bookRepository.searchAuthorFullText(word, size, pageOffset);
                    break;
                case "publisher":
                    booksList = bookRepository.searchPublisherFullText(word, size, pageOffset);
                    break;
                default:
                    booksList = bookRepository.searchIsbn(word, size, pageOffset);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    booksList = bookRepository.searchNativeTitleFullText(word, size, pageOffset);
                    break;
                case "author":
                    booksList = bookRepository.searchNativeAuthorFullText(word, size, pageOffset);
                    break;
                case "publisher":
                    booksList = bookRepository.searchNativePublisherFullText(word, size, pageOffset);
                    break;
                default:
                    booksList = bookRepository.searchIsbn(word, size, pageOffset);
                    break;
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


    @LogExecutionTime
    public ListBookResTestDtoAndPagination searchFullTextQueryDsl(String word, String mode,int page, int size, String field) {
        int count = 0;

        if(field.equals("isbn")) {
            count = bookRepository.searchByIsbnCountQuery(word, mode, field);
        } else {
            count = bookRepository.searchByFullTextBooleanCount(word, mode, field);
        }

        Pagination pagination = new Pagination(count, page);
        int pageOffset = pagination.getStartIndex();

        List<BookResTestDto> list = bookRepository.searchByFullTextBooleanTest(word, mode, pageOffset, size, field);

        return ListBookResTestDtoAndPagination.builder()
                .bookResTestDtoList(list)
                .pagination(pagination)
                .build();
    }

    public List<BookResTestDto> Excel(String word, String mode, String field) {

        List<BookResTestDto> BookResTestDto= bookRepository.forExcelQuery(word,mode,field);

        return BookResTestDto;
    }

    public List<BookResTestDto> JpqlExcel(String word, String mode, String field) {

        List<Books> booksList = new ArrayList<>();

        // List 가져오는 SEQ
        if (mode.equals("boolean mode")) {
            switch (field) {
                case "title":
                    booksList = bookRepository.titleForExcel(word);
                    break;
                case "author":
                    booksList = bookRepository.authorForExcel(word);
                    break;
                case "publisher":
                    booksList = bookRepository.publisherForExcel(word);
                    break;
                default:
                    booksList = bookRepository.isbnForExcel(word);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    booksList = bookRepository.titleNatureForExcel(word);
                    break;
                case "author":
                    booksList = bookRepository.authorNatureForExcel(word);
                    break;
                case "publisher":
                    booksList = bookRepository.publisherNatureForExcel(word);
                    break;
                default:
                    booksList = bookRepository.isbnForExcel(word);
                    break;
            }
        }

        List<BookResTestDto> bookResTestDtoList = new ArrayList<>();
        for (Books books : booksList) {
            bookResTestDtoList.add(new BookResTestDto(books));
        }
        return bookResTestDtoList;
    }

    public BookDetailResDto searchDetail(Long bookId, Long isbn, String username) {

        Books book = bookRepository.findById(bookId).orElseThrow();

        Member member = memberRepository.findByUsername(username).orElseThrow();

        Long rentalBook = bookRentalRepository.countByBook(book);

        Long bookCount = Long.parseLong(book.getBookCount()) - rentalBook;

        boolean record = bookRentalRepository.existsByBookAndMember(book, member);

        log.info("record : " + record);

        if(bookDetailRepository.existsByIsbn(isbn)){
            BookDetails bookDetail = bookDetailRepository.findByIsbn(isbn);
            return new BookDetailResDto(bookDetail, book, bookCount, record);
        }
        return new BookDetailResDto(book, bookCount, record);
    }

    public int rentalBook(Long bookId, String username) {

        Books book = bookRepository.findById(bookId).orElseThrow();

        Member member = memberRepository.findByUsername(username).orElseThrow();

        Long rentalBook = bookRentalRepository.countByBook(book);

        Long bookCount = Long.parseLong(book.getBookCount()) - rentalBook;

        if (bookRentalRepository.existsByBookAndMember(book, member)) {
            bookRentalRepository.deleteByBookAndMember(book, member);
            return 1;
        }

        if(bookCount > 0) {

            BookRentals bookRental = BookRentals.builder()
                    .book(book)
                    .member(member)
                    .build();

            StringBuilder sb = new StringBuilder();
            sb.append(book.getTitle()).append(member.getUsername()+"님이 대여하셨습니다.");

            sseController.publish(String.valueOf(sb));

            bookRentalRepository.save(bookRental);

            return 1;
        } else {
            return 0;
        }
    }

    public Long findIsbn(Long bookId) {
        return bookRepository.isbnFindById(bookId);
    }

    public List<BookInfiniteRepoResDto> getInfiniteBooksList(int lastId, int limitSize) {
        return bookRepository.searchBookListForInfinityScroll(lastId, limitSize);
    }

    public int searchInfinityCount() {
        return bookRepository.searchInfinityCountMaxNum();
    }

}
