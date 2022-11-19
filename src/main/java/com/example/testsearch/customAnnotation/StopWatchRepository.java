package com.example.testsearch.customAnnotation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StopWatchRepository extends JpaRepository<StopWatchTable, Long> {
    StopWatchTable findTopByOrderByIdDesc();
}
