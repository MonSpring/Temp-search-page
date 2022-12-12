package com.example.testsearch.repository;

import com.example.testsearch.dto.LibraryResDtoV2;
import com.example.testsearch.entity.Librarys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibrarysRepository extends JpaRepository<Librarys, Long>{

    @Query(value = "select l.lib_name, l.libcode from Librarys l", nativeQuery = true)
    List<LibraryResDtoV2> findByLibrary();

}
