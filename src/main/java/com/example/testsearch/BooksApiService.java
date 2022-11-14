package com.example.testsearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BooksApiService {

    private final BookRepository bookRepository;

    @Transactional
    public void getItems(String isbn) throws IOException, ParserConfigurationException, SAXException {

//        List<Books> books = bookRepository.findAllByIsbnContains(isbn);

        // 호출에 필요한 Header, Body 정리
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

//        http://data4library.kr/api/srchDtlList?authKey=98bf4d67611396d1609f4c5dec21a8d3095f34202d8c5d7b7f0c87d96c27f33e&isbn13=9788983921987&loaninfoYN=Y

        String authKey = "98bf4d67611396d1609f4c5dec21a8d3095f34202d8c5d7b7f0c87d96c27f33e";
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = rest.exchange("http://data4library.kr/api/srchDtlList?authKey=" + authKey + "&" + "isbn13=" + isbn + "&" + "loaninfoYN=Y", HttpMethod.GET, requestEntity, String.class);
        String booksApiResponseXml = responseEntity.getBody();

        // booksApiResponseJson (JSON 형태) -> BooksApiDto (자바 객체 형태)
        // booksApiResponseJson 에서 우리가 사용할 데이터만 추출 -> List<BooksApiDto> 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//        XML로 받으니까 XML로 받아서 자바로 변환해야함
        StringBuilder sb = new StringBuilder();
        //StringBuffer 만들어서 사용 할 것이기에 StringBuffer 선언

        sb.append(booksApiResponseXml);
        //선언한 StringBuffer안에 xml 형식 String 파일을 삽입

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Document로 파싱 하여 사용 할 것이기에 DocumentBuilderFactory 선언

        DocumentBuilder builder = factory.newDocumentBuilder();
        //DocumentBuilderFactory로 DocumentBuilder

        Document document = builder.parse(new InputSource(new StringReader(sb.toString())));
        //sb.toString을 Document 형식으로 저장

        NodeList responselist = document.getElementsByTagName("response");
        //document 안에서 찾고자 하는 태그값을 가져 와서 NodeList로 저장

        Node reponseText = responselist.item(0).getChildNodes().item(0);
        //NodeList는 List 형태 이기에 Node로 변환 하여 저장

        String resTag = reponseText.getNodeValue();
        // tagtext에 있는 값은 Node로 선언 되어 있어서 getNodeValue()로 String으로 변환 하여 저장

        log.info("sb : " + sb);
        log.info("document : " + document);
        log.info("responselist : " + responselist);
        log.info("reponseText : " + reponseText);
        log.info("resTag : " + resTag);


//        log.info(String.valueOf(itemsNode));
//        List<BooksApiDto> booksApiDtoList = objectMapper
//                .readerFor(new TypeReference<List<BooksApiDto>>() {})
//                .readValue(itemsNode);

//        return booksApiDtoList;
    }
}
