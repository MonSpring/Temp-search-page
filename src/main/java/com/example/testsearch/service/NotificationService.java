package com.example.testsearch.service;

import com.example.testsearch.dto.MessageResDto;
import com.example.testsearch.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<MessageResDto> getMessage() {
        return notificationRepository.getAll();
    }

}
