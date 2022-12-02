package com.example.testsearch.repository;

import com.example.testsearch.entity.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDetailRepository extends JpaRepository<BookDetails, Long> {

    BookDetails findByIsbn(Long isbn);
}
