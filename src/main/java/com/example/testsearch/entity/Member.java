package com.example.testsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Column(nullable = false)
    private String email;

    @Column(unique = true)
    private Long kakaoId;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Orders> ordersList = new ArrayList<>();

    @Builder
    public Member(String username, String password, String email, Authority authority) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.kakaoId = null;
        this.authority = authority;
    }

    public Member(String username, String encodedPassword, String email, Authority role, Long kakaoId) {
        this.username = username;
        this.password = encodedPassword;
        this.email = email;
        this.authority = role;
        this.kakaoId = kakaoId;
    }
}
