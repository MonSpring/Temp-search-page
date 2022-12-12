package com.example.testsearch.service;

import com.example.testsearch.controller.SseController;
import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.dto.*;
import com.example.testsearch.entity.*;
import com.example.testsearch.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final ElasticBooksRepository elasticBooksRepository;

    private final BookRepository bookRepository;

    private final BookDetailRepository bookDetailRepository;

    private final BookRentalRepository bookRentalRepository;

    private final MemberRepository memberRepository;

    private final SseController sseController;

    private final MemberTestRepository memberTestRepository;

    private final StringRedisTemplate stringRedisTemplate;

    private final BookRentalTestRepository bookRentalTestRepository;

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

        List<BookInfiniteRepoResDto> booksList;
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

        return ListBookResTestDtoAndPagination.builder()
                .bookResTestDtoList(booksList)
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

        List<BookResTestDto> BookResTestDto = bookRepository.forExcelQuery(word,mode,field);

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

    @Transactional
    public BookDetailResDto searchDetail(Long bookId, Long isbn, String username) {

        Books book = bookRepository.findById(bookId).orElseThrow();

        Member member = memberRepository.findByUsername(username).orElseThrow();

        Long rentalBook = bookRentalRepository.countByBook(book);

        Long bookCount = Long.parseLong(book.getBookCount()) - rentalBook;

        boolean record = bookRentalRepository.existsByBookAndMember(book, member);

        if(bookDetailRepository.existsByIsbn(isbn)){
            BookDetails bookDetail = bookDetailRepository.findByIsbn(isbn);
            return new BookDetailResDto(bookDetail, book, bookCount, record);
        }
        return new BookDetailResDto(book, bookCount, record);
    }

    @Transactional
    public String rentalBook(Long bookId, String username) {

        Books book = bookRepository.findById(bookId).orElseThrow();

        Member member = memberRepository.findByUsername(username).orElseThrow();

        if(Long.parseLong(book.getBookCount()) - bookRentalRepository.countByBook(book) > 0) {

            BookRentals bookRental = BookRentals.builder()
                    .book(book)
                    .member(member)
                    .build();

            StringBuilder sb = new StringBuilder();
            /*sb.append("대여 안내 : ").append(book.getTitle()).append("도서를 ").append(member.getUsername()).append("님이 대여하셨습니다.");*/
            sb.append("대여");

            sseController.publish(String.valueOf(sb));

            bookRentalRepository.save(bookRental);

            return String.valueOf(sb);
        } else {
            return "";
        }
    }

    @Transactional
    public String returnBook(Long bookId, String username){

        StringBuilder sb = new StringBuilder();

        Books book = bookRepository.findById(bookId).orElseThrow();

        Member member = memberRepository.findByUsername(username).orElseThrow();

        bookRentalRepository.deleteByBookAndMember(book, member);

        sb.append("반납완료");

        return String.valueOf(sb);
    }

    public boolean isValidationRental(String username) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

        if(stringStringValueOperations.get(String.valueOf(username)) != null) {
            return false;
        }

        // 1초
        long LIMIT_TIME = 1000;
        stringStringValueOperations.set(username, username, LIMIT_TIME, TimeUnit.MILLISECONDS);

        return true;
    }

    @Transactional
    public String rentalBookTest(Long bookId, Long memberId) {

        StringBuilder sb = new StringBuilder();

        if(isValidationRentalTest(memberId)) {

            if(bookRentalTestRepository.existsByBookIdAndMemberId(bookId, memberId) > 0){
                sb.append("중복대여");
            } else {

                /*
                BookRentalTest bookRental = BookRentalTest.builder()
                        .book(book)
                        .member(member)
                        .build();

                bookRentalTestRepository.save(bookRental);
                */

                    bookRentalTestRepository.saveBookRentalTest(bookId, memberId);

                    sb.append("대여완료");

                    sseController.publish(String.valueOf(sb));

            }
        } else {
            sb.append("중복클릭");
        }
        return String.valueOf(sb);
    }

    public Long countRentalBookTest(Long bookId) {

        Books book = bookRepository.findById(bookId).orElseThrow();

        Long rentalBook = bookRentalTestRepository.countByBook(book);

        Long bookCount = Long.parseLong(book.getBookCount()) - rentalBook;

        return bookCount;
    }

    public boolean isValidationRentalTest(Long memberId) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

        if(stringStringValueOperations.get(String.valueOf(memberId)) != null) {
            return false;
        }

        // 1초
        long LIMIT_TIME = 3000;
        stringStringValueOperations.set(String.valueOf(memberId), String.valueOf(memberId), LIMIT_TIME, TimeUnit.MILLISECONDS);

        return true;
    }
    public boolean isValidationRentalCountTest(Long bookCount) {

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

        if(bookCount < 1 || stringStringValueOperations.get(String.valueOf(bookCount)).equals("0")) {
            return false;
        }

        // 1초
        long LIMIT_TIME = 1000;
        stringStringValueOperations.set(String.valueOf(bookCount), String.valueOf(bookCount), LIMIT_TIME, TimeUnit.MILLISECONDS);

        return true;
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

    @Transactional
    @LogExecutionTime
    public ListElasticBookResTestDtoAndPagination getElasticBooksSearch(String word, int size, int page, String field, String mode) {

        int pageTemp = page - 1;
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "book_id");
        Pageable pageable = PageRequest.of(pageTemp, size, sort);
        int totalListCnt = 0;
        List<ElasticBooks> elasticBooksList;

        // Total Rows 가져오는 SEQ
        if (mode.equals("text")) {
            switch (field) {
                case "title":
                    totalListCnt = elasticBooksRepository.countAllByTitleContains(word);
                    break;
                case "author":
                    totalListCnt = elasticBooksRepository.countAllByAuthorContains(word);
                    break;
                case "publisher":
                    totalListCnt = elasticBooksRepository.countAllByPublisherContains(word);
                    break;
                default:
                    totalListCnt = elasticBooksRepository.countAllByIsbnContains(word);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    totalListCnt = elasticBooksRepository.countAllByTitleKeywordContains(word);
                    break;
                case "author":
                    totalListCnt = elasticBooksRepository.countAllByAuthorKeywordContains(word);
                    break;
                case "publisher":
                    totalListCnt = elasticBooksRepository.countAllByPublisherKeywordContains(word);
                    break;
                default:
                    totalListCnt = elasticBooksRepository.countAllByIsbnContains(word);
                    break;
            }
        }

        Pagination pagination = new Pagination(totalListCnt, page);
        log.info("total rows : " + totalListCnt);

        // List 가져오는 SEQ
        if (mode.equals("text")) {
            switch (field) {
                case "title":
                    elasticBooksList = elasticBooksRepository.findAllByTitleContains(word, pageable);
                    break;
                case "author":
                    elasticBooksList = elasticBooksRepository.findAllByAuthorContains(word, pageable);
                    break;
                case "publisher":
                    elasticBooksList = elasticBooksRepository.findAllByPublisherContains(word, pageable);
                    break;
                default:
                    elasticBooksList = elasticBooksRepository.findAllByIsbnContains(word, pageable);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    elasticBooksList = elasticBooksRepository.findAllByTitleKeywordContains(word, pageable);
                    break;
                case "author":
                    elasticBooksList = elasticBooksRepository.findAllByAuthorKeywordContains(word, pageable);
                    break;
                case "publisher":
                    elasticBooksList = elasticBooksRepository.findAllByPublisherKeywordContains(word, pageable);
                    break;
                default:
                    elasticBooksList = elasticBooksRepository.findAllByIsbnContains(word, pageable);
                    break;
            }
        }

        List<ElasticBooksResDto> elasticBooksResDtoList = new ArrayList<>();

        for (ElasticBooks books:elasticBooksList) {
            ElasticBooksResDto elasticBooksResDto = new ElasticBooksResDto(books);
            elasticBooksResDtoList.add(elasticBooksResDto);
        }

        return ListElasticBookResTestDtoAndPagination.builder()
                .elasticBooksResDtoList(elasticBooksResDtoList)
                .pagination(pagination)
                .build();
    }

    public List<ElasticBooksResDto> searchElasticForExcel(String word, String mode, String field) {

        List<ElasticBooks> elasticBooksList;

        if (mode.equals("text")) {
            switch (field) {
                case "title":
                    elasticBooksList = elasticBooksRepository.findAllByTitleContains(word);
                    break;
                case "author":
                    elasticBooksList = elasticBooksRepository.findAllByAuthorContains(word);
                    break;
                case "publisher":
                    elasticBooksList = elasticBooksRepository.findAllByPublisherContains(word);
                    break;
                default:
                    elasticBooksList = elasticBooksRepository.findAllByIsbnContains(word);
                    break;
            }
        } else {
            switch (field) {
                case "title":
                    elasticBooksList = elasticBooksRepository.findAllByTitleKeywordContains(word);
                    break;
                case "author":
                    elasticBooksList = elasticBooksRepository.findAllByAuthorKeywordContains(word);
                    break;
                case "publisher":
                    elasticBooksList = elasticBooksRepository.findAllByPublisherKeywordContains(word);
                    break;
                default:
                    elasticBooksList = elasticBooksRepository.findAllByIsbnContains(word);
                    break;
            }
        }
        List<ElasticBooksResDto> elasticBooksResDtoList = new ArrayList<>();

        for (ElasticBooks books:elasticBooksList) {
            ElasticBooksResDto elasticBooksResDto = new ElasticBooksResDto(books);
            elasticBooksResDtoList.add(elasticBooksResDto);
        }

        return elasticBooksResDtoList;
    }

    public void outputExcelForElastic(List<ElasticBooksResDto> elasticBooksResDtoList, HttpServletResponse res) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("책이름");
        cell = row.createCell(2);
        cell.setCellValue("작가");
        cell = row.createCell(3);
        cell.setCellValue("출판사");
        cell = row.createCell(4);
        cell.setCellValue("권수");
        cell = row.createCell(5);
        cell.setCellValue("isbn");

        for (int i = 0; i < elasticBooksResDtoList.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(elasticBooksResDtoList.get(i).getTitle());
            cell = row.createCell(2);
            cell.setCellValue(elasticBooksResDtoList.get(i).getAuthor());
            cell = row.createCell(3);
            cell.setCellValue(elasticBooksResDtoList.get(i).getPublisher());
            cell = row.createCell(4);
            cell.setCellValue(elasticBooksResDtoList.get(i).getBookCount());
            cell = row.createCell(5);
            cell.setCellValue(elasticBooksResDtoList.get(i).getIsbn());
        }

        res.setContentType("ms-vnd/excel");
        res.setHeader("Content-Disposition", "attachment;filename=excel.xlsx");

        wb.write(res.getOutputStream());
    }

    public List<Books> searchLibrary(Long libcode) {
        return bookRepository.getBooksByLibrarys(libcode);
    }

/*    public List<LibrarysResDto> searchLibraryV2(Long libcode) {
        return bookRepository.getBooksByLibrarysV2(libcode);
    }*/
}
