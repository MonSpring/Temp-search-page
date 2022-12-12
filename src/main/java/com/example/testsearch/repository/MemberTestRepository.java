package com.example.testsearch.repository;

import com.example.testsearch.entity.MemberTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTestRepository extends JpaRepository<MemberTest, Long> {

    Optional<MemberTest> findByMemberId(Long username);
}
