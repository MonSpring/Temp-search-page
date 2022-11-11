package com.example.testsearch.customAnnotation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class StopWatchTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String method;

    @Column
    private Long mills;

    @Column
    private Long nanos;

    @Builder
    public StopWatchTable(String method, Long mills, Long nanos) {
        this.method = method;
        this.mills = mills;
        this.nanos = nanos;
    }
}
