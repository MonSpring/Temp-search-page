package com.example.testsearch.repository;

import com.example.testsearch.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Member> findByKakaoId(Long kakaoId);
    Optional<Member> findByEmail(String email);
}