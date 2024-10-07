package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // we can add the pageable parameter at the end of the query bcos we've add the pageable here
    // below we have created our own findByTitleContaining search API and passing in the title, and we're making it pageable
    // URL- http://localhost:8080/api/books/search/findByTitleContaining?title=guru&page=0&size=5
    Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);

    // Creating a  findByCategory search API
    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);

    // here below we're passing out a list of Ids so we;re going to have a little bit more advanced SQL commands, which means now we need to be able to query and create our own find booksByBooId.
    // so spring knows exactly what to do when this function is called.
    // Using @Query annotation - here below o is just the representation of each element in bookIds
    @Query("select o from Book o where id in :book_ids")
    List<Book> findBooksByBookIds(@Param("book_ids") List<Long> bookId);



}
