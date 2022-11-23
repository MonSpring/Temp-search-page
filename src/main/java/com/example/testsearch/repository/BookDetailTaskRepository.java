package com.example.testsearch.repository;

import com.example.testsearch.entity.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDetailTaskRepository extends JpaRepository<BookDetails, Long> {

    BookDetails findTop1ByOrderByIsbnAsc();
}
