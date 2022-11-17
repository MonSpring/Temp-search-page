package com.example.testsearch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderBooks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderBook_id")
    private Long id;

    @Column
    private int price;

    @Column
    private int count;

    @JoinColumn(name = "books_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Books books;

    @JoinColumn(name = "orders_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @Builder
    public OrderBooks(Long id, int price, int count, Books books, Orders orders) {
        this.id = id;
        this.price = price;
        this.count = count;
        this.books = books;
        this.orders = orders;
    }

    public void changeOrders(Orders orders) {
        this.orders = orders;
        orders.getOrderBooksList().add(this);
    }
}
