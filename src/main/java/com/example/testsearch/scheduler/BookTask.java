package com.example.testsearch.scheduler;

import com.example.testsearch.customAnnotation.LogExecutionTime;
import com.example.testsearch.entity.BookDetails;
import com.example.testsearch.repository.BookDetailTaskRepository;
import com.example.testsearch.repository.BookTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookTask {

    @Value("#{environment['naru.key']}")
    public String informationNaruKey;

    private final BookTaskRepository bookTaskRepository;

    private final BookDetailTaskRepository bookDetailTaskRepository;

    private final JdbcTemplate jdbcTemplate;

    // 하루 500 건씩 가져올 수 있음
    //@Scheduled(cron = "0 0/7 * * * *")
    // 10초에 한번씩 테스트
    //@Scheduled(cron = "0 0/10 * * * *")
    public void getBookDetailTask() {

        List<Long> isbnList = new ArrayList<>();

        Long deatilMinIsbn;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;

        ArrayList<BookDetails> bookDetails  = new ArrayList<>();

        if (bookDetailTaskRepository.count() == 0) {
            isbnList = bookTaskRepository.findTopByOrderByIsbnDesc();
        } else {
            deatilMinIsbn = bookDetailTaskRepository.findTop1ByOrderByIsbnAsc().getIsbn();

            isbnList = bookTaskRepository.findTopByIsbnLessThanOrderByIsbnDesc(deatilMinIsbn);
        }
        for (Long aLong : isbnList) {
            try {

                String apiURL = "http://data4library.kr/api/srchDtlList?authKey=" + informationNaruKey
                        + "&isbn13=" + aLong + "&loaninfoYN=N";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();
                if ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                String responseXml = response.toString();

                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(responseXml)));



                if (document.getElementsByTagName("error").getLength() > 0) {
                    log.info(document.getElementsByTagName("error").item(0).getChildNodes().item(0).getNodeValue());

                    BookDetails saveData = BookDetails.builder()
                            .isbn(aLong)
                            .description(null)
                            .thumbnail(null)
                            .build();

                    bookDetails.add(saveData);
                } else {

                    String responseIsbn = document.getElementsByTagName("isbn13").item(0).getChildNodes().item(0).getNodeValue();
                    String responseDescription = null;
                    String responseBookImageURL = null;

                    if (document.getElementsByTagName("description").item(0).getChildNodes().getLength() > 0) {
                        responseDescription = document.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
                    }
                    if (document.getElementsByTagName("bookImageURL").item(0).getChildNodes().getLength() > 0) {
                        responseBookImageURL = document.getElementsByTagName("bookImageURL").item(0).getChildNodes().item(0).getNodeValue();
                    }

                    BookDetails saveData = BookDetails.builder()
                            .isbn(Long.valueOf(responseIsbn))
                            .description(responseDescription)
                            .thumbnail(responseBookImageURL)
                            .build();

                    bookDetails.add(saveData);
                }
            } catch(SAXParseException e){
                log.error(e.toString());
            } catch(Exception e){
                log.error(e.toString());
            }
        }
        bookDetailTaskRepository.saveAll(bookDetails);
    }

    // 4시 45분에 가져온 데이터중 NULL값 삭제
//    @Scheduled(cron = "0 21 6 * * *")
    public void deleteNullDataTask() {
        Long minimumIsbn = bookDetailTaskRepository.findTop1ByOrderByIsbnAsc().getIsbn();

        List<Long> isbnList = bookDetailTaskRepository.findIsbnExceptMinimun(minimumIsbn);

        bookDetailTaskRepository.deleteAllByIsbnIn(isbnList);
    }


    // jdbc 방식
    @LogExecutionTime
//    @Scheduled(cron = "0 0/7 * * * *")
    // 10초에 한번씩 테스트
    //@Scheduled(cron = "0 0/10 * * * *")
    public void getBookDetailTask2() {

        List<Long> isbnList = new ArrayList<>();

        Long deatilMinIsbn;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;

        ArrayList<BookDetails> bookDetails  = new ArrayList<>();

        if (bookDetailTaskRepository.count() == 0) {
            isbnList = bookTaskRepository.findTopByOrderByIsbnDesc();
        } else {
            deatilMinIsbn = bookDetailTaskRepository.findTop1ByOrderByIsbnAsc().getIsbn();

            isbnList = bookTaskRepository.findTopByIsbnLessThanOrderByIsbnDesc(deatilMinIsbn);
        }
        for (Long aLong : isbnList) {
            try {

                String apiURL = "http://data4library.kr/api/srchDtlList?authKey=" + informationNaruKey
                        + "&isbn13=" + aLong + "&loaninfoYN=N";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();
                if ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                String responseXml = response.toString();

                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(responseXml)));



                if (document.getElementsByTagName("error").getLength() > 0) {
                    log.info(document.getElementsByTagName("error").item(0).getChildNodes().item(0).getNodeValue());

                    BookDetails saveData = BookDetails.builder()
                            .isbn(aLong)
                            .description(null)
                            .thumbnail(null)
                            .build();

                    bookDetails.add(saveData);
                } else {

                    String responseIsbn = document.getElementsByTagName("isbn13").item(0).getChildNodes().item(0).getNodeValue();
                    String responseDescription = null;
                    String responseBookImageURL = null;

                    if (document.getElementsByTagName("description").item(0).getChildNodes().getLength() > 0) {
                        responseDescription = document.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
                    }
                    if (document.getElementsByTagName("bookImageURL").item(0).getChildNodes().getLength() > 0) {
                        responseBookImageURL = document.getElementsByTagName("bookImageURL").item(0).getChildNodes().item(0).getNodeValue();
                    }

                    BookDetails saveData = BookDetails.builder()
                            .isbn(Long.valueOf(responseIsbn))
                            .description(responseDescription)
                            .thumbnail(responseBookImageURL)
                            .build();

                    bookDetails.add(saveData);
                }
            } catch(SAXParseException e){
                log.error(e.toString());
            } catch(Exception e){
                log.error(e.toString());
            }
        }
        int batchSize = 1000;
        String sql = "INSERT INTO book_details (description , isbn , thumbnail) values (?,?,?)";
        jdbcTemplate.batchUpdate(sql,bookDetails,batchSize, ((ps, arg) -> {
            ps.setString(1, arg.getDescription());
            ps.setLong(2, arg.getIsbn());
            ps.setString(3, arg.getThumbnail());
        }));
    }


}
