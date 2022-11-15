package com.example.testsearch.entity;


import com.example.testsearch.entity.Books;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter @Getter
@NoArgsConstructor
public class Librarys {

    @Id
    private Long libcode;

    @Column(name = "lib_name")
    private String libName;

    @Column
    private String address;

    @Column
    private String tel;

    @Column
    private String fax;

    @Column
    private float latitude;

    @Column
    private float longitude;

    @Column
    private String homepage;

    @Column
    private String closed;

    @OneToMany(mappedBy = "librarys")
    private List<Books> booksList = new ArrayList<>();

    @Builder
    public Librarys(Long libcode, String libName, String address, String tel, String fax, float latitude, float longitude, String homepage, String closed) {
        this.libcode = libcode;
        this.libName = libName;
        this.address = address;
        this.tel = tel;
        this.fax = fax;
        this.latitude = latitude;
        this.longitude = longitude;
        this.homepage = homepage;
        this.closed = closed;
    }

    public void update(Books book) {
        book.setLibrarys(this);
        getBooksList().add(book);
    }
}
