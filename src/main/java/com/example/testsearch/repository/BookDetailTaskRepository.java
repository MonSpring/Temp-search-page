package com.example.testsearch.repository;

import com.example.testsearch.entity.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BookDetailTaskRepository extends JpaRepository<BookDetails, Long> {

    BookDetails findTop1ByOrderByIsbnAsc();

    @Query("SELECT b.isbn FROM BookDetails b WHERE (b.description IS NULL or b.thumbnail IS NULL) AND b.isbn > :minimumIsbn")
    List<Long> findIsbnExceptMinimun(Long minimumIsbn);

    @Modifying
    @Transactional
    @Query("delete from BookDetails b where b.isbn in :isbnList")
    void deleteAllByIsbnIn(List<Long> isbnList);

}
