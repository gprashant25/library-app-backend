package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookId(@RequestParam("book_id")Long bookId, Pageable pageable);

    Review findByUserEmailAndBookId(String userEmail, Long bookId);


    // IMPORTANT: below code is written if the admin wants to delete a specific book, then we also want to delete all of the reviews and current checkouts of that specific book having it with it.
    // So when we delete a book,  we want to delete all the review repositories that have the bookId as a column in the review database
    @Modifying   // this annotation is used bcos we're going to be modifying the record ie going to delete the record in the database
    @Query("delete from Review where bookId in :book_id")             // IMP: here inside the @Query annotation , write bookId in :book_id bcos the bookId is only mentioned as column name in the Review Entity as well. ie if we want to delete all the review repositories that have the bookId as a column in the review database.
    void deleteAllByBookId(@Param("book_id")Long bookId);
}
