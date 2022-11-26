package com.example.testsearch.util;

import com.example.testsearch.entity.MemberLoginInfo;
import com.example.testsearch.repository.MemberLoginInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogMemberAddressService {

    private final MemberLoginInfoRepository memberLoginInfoRepository;

    public List<MemberLoginInfoResDto> searchAllMemberAddressLog(String startDatetime, String endDatetime) {

        // 타임존 설정 하는 법
        // ZoneId zoneid = ZoneId.of("Asia/Seoul");
        // long startDatetimeFromatMillSecond = startDatetimeFormatTemp.atZone(zoneid).toInstant().toEpochMilli();
        // long endDateTimeFromatMillSecond = endDateTimeFormatTemp.atZone(zoneid).toInstant().toEpochMilli();
        
        // 엘라스틱 서치에서 한달이 지나면 저절로 삭제되는 구문 만드는 중

        String startDatetimeTemp = startDatetime + " 00:00:00";
        String endDatetimeTemp = endDatetime + " 24:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetimeFormatTemp = LocalDateTime.parse(startDatetimeTemp, formatter);
        LocalDateTime endDateTimeFormatTemp = LocalDateTime.parse(endDatetimeTemp, formatter);

        // 엘라스틱서치 내부 Date 상태 "creation_date": "1669465728408"
        // 얘가 내부적으로 SEQ 두번 날리는 듯
        Iterable<MemberLoginInfo> memberLoginInfoList = memberLoginInfoRepository.findAllByLoginTimeBetweenOrderByLoginTimeDesc(startDatetimeFormatTemp, endDateTimeFormatTemp);
        List<MemberLoginInfoResDto> memberLoginInfoResDtoList = new ArrayList<>();

        for (MemberLoginInfo memberLoginInfo:memberLoginInfoList) {
            MemberLoginInfoResDto memberLoginInfoResDto = MemberLoginInfoResDto.builder()
                    .username(memberLoginInfo.getUsername())
                    .memberIp(memberLoginInfo.getMemberIp())
                    .loginTime(memberLoginInfo.getLoginTime())
                    .build();
            memberLoginInfoResDtoList.add(memberLoginInfoResDto);
        }
        return memberLoginInfoResDtoList;
    }
}
