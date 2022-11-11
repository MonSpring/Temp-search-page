//package com.example.testsearch;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class BooksApiService {
//
//    private final BooksRepository booksRepository;
//    public void getItems(Long isbn) throws IOException {
//
////        Books books = booksRepository.findByIsbnContains(isbn);
////
////        log.info(String.valueOf(books));
//
////        // 호출에 필요한 Header, Body 정리
////        RestTemplate rest = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////
//////        http://data4library.kr/api/srchDtlList?authKey=98bf4d67611396d1609f4c5dec21a8d3095f34202d8c5d7b7f0c87d96c27f33e&isbn13=9788983921987&loaninfoYN=Y
////
////        String authKey = "98bf4d67611396d1609f4c5dec21a8d3095f34202d8c5d7b7f0c87d96c27f33e";
////        String body = "";
////        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
////
////        ResponseEntity<String> responseEntity = rest.exchange("http://data4library.kr/api/srchDtlList?authKey=" + authKey + "&" + isbn + "&" + "loaninfoYN=Y", HttpMethod.GET, requestEntity, String.class);
////        String booksApiResponseJson = responseEntity.getBody();
////
////        // booksApiResponseJson (JSON 형태) -> BooksApiDto (자바 객체 형태)
////        // booksApiResponseJson 에서 우리가 사용할 데이터만 추출 -> List<BooksApiDto> 객체로 변환
////        ObjectMapper objectMapper = new ObjectMapper()
////                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
////        JsonNode itemsNode = objectMapper.readTree(booksApiResponseJson).get("response");
////
////        log.info(String.valueOf(itemsNode));
//////        List<BooksApiDto> booksApiDtoList = objectMapper
//////                .readerFor(new TypeReference<List<BooksApiDto>>() {})
//////                .readValue(itemsNode);
////
//////        return booksApiDtoList;
//    }
//}
