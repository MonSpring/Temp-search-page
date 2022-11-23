package com.example.testsearch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class Scheduler {

    @Value("#{environment['naru.key']}")
    public String informationNaruKey;

    // 500 건씩 가져올 수 있음
    // 새벽 5시
    /*@Scheduled(cron = "* * 5 * * *")*/
    // 10초에 한번씩
    @Scheduled(cron = "0/10 * * * * *")
    public void getBookDetail() throws IOException, SAXException {
        try {

            long bookIsbn = 9791159318573L;

            String apiURL = "http://data4library.kr/api/srchDtlList?authKey=" + informationNaruKey
                    + "&isbn13=" + bookIsbn + "&loaninfoYN=Y";
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
            log.info(response.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }

        // 테스트용 데이터
        String xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<response>" +
                "<request>" +
                "<isbn13>9791159318573</isbn13>" +
                "<loaninfoYN>Y</loaninfoYN>" +
                "</request>" +
                "<detail>" +
                "<book>" +
                "<no>1</no>" +
                "<bookname><![CDATA[폭락장에도 텐배거는 있다]]></bookname>" +
                "<authors><![CDATA[강준혁 지음]]></authors>" +
                "<publisher><![CDATA[해의시간]]></publisher>" +
                "<class_no><![CDATA[327.856]]></class_no>" +
                "<class_nm><![CDATA[사회과학 > 경제학 > 금융]]></class_nm>" +
                "<publication_year><![CDATA[2022]]></publication_year>" +
                "<publication_date><![CDATA[2022]]></publication_date>" +
                "<bookImageURL><![CDATA[https://image.aladin.co.kr/product/29839/38/cover/k812838824_1.jpg]]></bookImageURL>" +
                "<isbn><![CDATA[1159318573]]></isbn>\n" +
                "<isbn13><![CDATA[9791159318573]]></isbn13>\n" +
                "<description><![CDATA[주식 투자별 개념부터 각각에 특화된 전략과 정보를 알려주는 책이다. 자신의 투자에 필요도 없는 시간과 감정을 소모해봤자 수익은 나지 않는다. 투자마다 필요한 것만 알면, 수익도 알아서 온다.]]></description>" +
                "</book>" +
                "</detail>" +
                "<loanInfo>" +
                "<Total></Total>" +
                "<regionResult></regionResult>" +
                "<ageResult></ageResult>" +
                "<genderResult></genderResult>" +
                "</loanInfo>" +
                "</response>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlTest)));

            NodeList tagList = document.getElementsByTagName("bookname");

            Node tagText = tagList.item(0).getChildNodes().item(0);

            String tag = tagText.getNodeValue();

            log.info(tag);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
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
