package com.example.testsearch.mailing;

import com.example.testsearch.customAnnotation.StopWatchTable;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sosaejiparty@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        emailSender.send(message);
    }

    public void sendMailDevelopersToSlowQuery(StopWatchTable stopWatchTable) {
        // 수신 대상 담을 리스트
        ArrayList<String> toUserList = new ArrayList<>();

        // 수신 대상 추가
        toUserList.add("kimyuntae19@gmail.com");
        toUserList.add("lcy122225@gmail.com");
        toUserList.add("ahnyi527@gmail.com");

        // 수신 대상 개수
        int toUserSize = toUserList.size();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 수신자 설정
        simpleMailMessage.setTo(toUserList.toArray(new String[toUserSize]));

        // 메일 제목
        simpleMailMessage.setSubject("Slow Query Issue Generated!");

        // 메일 내용
        simpleMailMessage.setText(stopWatchTable.getMethod() + " 서비스 메소드에서 " + stopWatchTable.getMills() / 1000 + "초 이상 걸리는 Slow Query가 발생했습니다");

        // 메일 발송
        emailSender.send(simpleMailMessage);
    }
}