package com.example.testsearch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Orders extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "orders")
    private List<OrderBooks> orderBooksList = new ArrayList<>();

    @Builder
    public Orders(Long id, Member member) {
        this.id = id;
        this.member = member;
    }

    public void changeOrderForMember(Member member) {
        this.member = member;
        member.getOrdersList().add(this);
    }
}
