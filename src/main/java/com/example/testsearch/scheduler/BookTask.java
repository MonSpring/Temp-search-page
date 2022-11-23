package com.example.testsearch.scheduler;

import com.example.testsearch.entity.BookDetails;
import com.example.testsearch.repository.BookDetailTaskRepository;
import com.example.testsearch.repository.BookTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookTask {

    // List 돌려서 각 key값으로 변경 예정 (하루 2,000건 가능)
    @Value("#{environment['naru.key']}")
    public String informationNaruKey;

    private final BookTaskRepository bookTaskRepository;

    private final BookDetailTaskRepository bookDetailTaskRepository;

    // 하루 500 건씩 가져올 수 있음
    @Scheduled(cron = "0 30 4 * * *")
    // 10초에 한번씩 테스트
    /*@Scheduled(cron = "0/10 * * * * *")*/
    public void getBookDetail() {

        List<Long> isbnList = new ArrayList<>();

        Long maxIsbn;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;

        if(bookDetailTaskRepository.count() == 0){
            isbnList = bookTaskRepository.findTopByOrderByIsbnDesc();
        } else {
                maxIsbn = bookDetailTaskRepository.findTop1ByOrderByIsbnAsc().getIsbn();

                isbnList = bookTaskRepository.findTopByIsbnLessThanOrderByIsbnDesc(maxIsbn);
        }

        try {

            for (Long aLong : isbnList) {

                String apiURL = "http://data4library.kr/api/srchDtlList?authKey=" + informationNaruKey
                        + "&isbn13=" + aLong + "&loaninfoYN=Y";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader br;

                if(responseCode==200) { // 정상 호출
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

                if(document.getElementsByTagName("error").getLength() > 0){
                    log.info(document.getElementsByTagName("error").item(0).getChildNodes().item(0).getNodeValue());

                    BookDetails saveData = BookDetails.builder()
                            .isbn(aLong)
                            .description(null)
                            .thumbnail(null)
                            .build();

                    bookDetailTaskRepository.save(saveData);

                } else {

                    String responseIsbn = document.getElementsByTagName("isbn13").item(0).getChildNodes().item(0).getNodeValue();
                    String responseDescription = null;
                    String responseBookImageURL = null;

                    if(document.getElementsByTagName("description").item(0).getChildNodes().getLength() > 0) {
                        responseDescription = document.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
                    }
                    if(document.getElementsByTagName("bookImageURL").item(0).getChildNodes().getLength() > 0) {
                        responseBookImageURL = document.getElementsByTagName("bookImageURL").item(0).getChildNodes().item(0).getNodeValue();
                    }

                    BookDetails saveData = BookDetails.builder()
                            .isbn(Long.valueOf(responseIsbn))
                            .description(responseDescription)
                            .thumbnail(responseBookImageURL)
                            .build();

                    bookDetailTaskRepository.save(saveData);
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

/*

        // 테스트용 데이터
        String xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<response>\n" +
                "<request>\n" +
                "<isbn13>9999003658572</isbn13>\n" +
                "<loaninfoYN>Y</loaninfoYN>\n" +
                "</request>\n" +
                "<detail>\n" +
                "<book>\n" +
                "<no>1</no>\n" +
                "<bookname><![CDATA[CONTOS FLUMINENSES]]></bookname>\n" +
                "<authors><![CDATA[MACHADO DE ASSIS]]></authors>\n" +
                "<publisher><![CDATA[CARNIER]]></publisher>\n" +
                "<class_no><![CDATA[801]]></class_no>\n" +
                "<class_nm><![CDATA[문학 > 문학 > 문학이론]]></class_nm>\n" +
                "<publication_year><![CDATA[1989]]></publication_year>\n" +
                "<publication_date><![CDATA[1989]]></publication_date>\n" +
                "<bookImageURL/>\n" +
                "<isbn><![CDATA[9003658579]]></isbn>\n" +
                "<isbn13><![CDATA[9999003658572]]></isbn13>\n" +
                "<description/>\n" +
                "</book>\n" +
                "</detail>\n" +
                "<loanInfo>\n" +
                "<Total></Total>\n" +
                "<regionResult></regionResult>\n" +
                "<ageResult></ageResult>\n" +
                "<genderResult></genderResult>\n" +
                "</loanInfo>\n" +
                "</response>";

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlTest)));

            NodeList tagList = document.getElementsByTagName("isbn13");

            Node tagText = tagList.item(0).getChildNodes().item(0);

            String tag = tagText.getNodeValue();

            log.info(tag);

            log.info("에러 테스트 : " + document.getElementsByTagName("isbn13").item(0).getChildNodes().getLength());
            log.info("에러 테스트 : " + document.getElementsByTagName("bookImageURL").item(0).getChildNodes().getLength());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
*/

    }

    // 4시 40분에 가져온 데이터중 NULL값 삭제
    @Scheduled(cron = "0 40 4 * * *")
    public void fixedDelayScheduler() {
        Long minimumIsbn = bookDetailTaskRepository.findTop1ByOrderByIsbnAsc().getIsbn();

        List<Long> isbnList = bookDetailTaskRepository.findIsbnExceptMinimun(minimumIsbn);

        bookDetailTaskRepository.deleteAllByIsbnIn(isbnList);
    }

    // 카카오 OPEN API (데이터 부정확함)
    /*@Scheduled(cron = "0 0 3 * * *")*/
    public void kakaoGetBookDetail(){
        try {
            long bookIsbn = 9791160949612L;
            String kakaoKey = "4ae013ba08eac80bfa8bb08ad6636365";

            String apiURL = "https://dapi.kakao.com/v2/search/web?target=isbn&sort=accuracy&page=1&size=1&query=isbn+%3A+9791160949612" + bookIsbn;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + kakaoKey);
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
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
            log.info(response.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }


}
