package com.example.testsearch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ui.Model;

@Getter @Setter
public class BooksRentalIntAndModelResDto {

    private Model model;
    private int succesCode;

    @Builder
    public BooksRentalIntAndModelResDto(Model model, int succesCode) {
        this.model = model;
        this.succesCode = succesCode;
    }
}
