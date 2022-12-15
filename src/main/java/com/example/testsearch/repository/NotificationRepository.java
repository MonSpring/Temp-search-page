package com.example.testsearch.repository;

import com.example.testsearch.dto.MessageResDto;
import com.example.testsearch.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Message, Long> {

    @Query(value = "select m from Message m")
    List<MessageResDto> getAll();

}
