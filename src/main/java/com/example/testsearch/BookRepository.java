package com.example.testsearch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository  extends JpaRepository<Books, Long> {

}
