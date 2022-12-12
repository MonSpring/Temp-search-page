package com.example.testsearch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class MemberTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
}
