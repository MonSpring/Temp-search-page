package com.example.testsearch.service;

import com.example.testsearch.repository.BookRepository;
import com.example.testsearch.xml.XmlListTag;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class BooksApiService {

    private final BookRepository bookRepository;

    @Transactional
    public void getItems(String isbn) throws JAXBException {

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

        log.info("booksApiResponseXml : " + booksApiResponseXml);

        // Given
        assert booksApiResponseXml != null;
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlListTag.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // When
        XmlListTag xmlListTag = (XmlListTag) unmarshaller.unmarshal(new StringReader(booksApiResponseXml));

        // Then
        log.info("xmlListTag : " + xmlListTag);
        log.info("xmlListTag getResponseTags : " + xmlListTag.getResponseTags().toString());
        log.info("xmlListTag getBookTag : " + xmlListTag.getResponseTags().getDetails().getBookTag().toString());

//        assertNotNull(xmlListTag);
//        assertNotNull(xmlListTag.getSmartPhoneTags());
//        assertEquals(4, xmlListTag.getSmartPhoneTags().length);
//        assertEquals("iPhone", xmlListTag.getSmartPhoneTags()[0].getModel());
    }
}
