package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    // creating a new interface function to find how many checkouts a particular user has in total
    List<Checkout> findBooksByUserEmail(String userEmail);

    // IMPORTANT: below code is written if the admin wants to delete a specific book, then we also want to delete all of the reviews and current checkouts of that specific book having it with it.
    // So when we delete a book,  we want to delete all the checkout repositories that have the bookId as a column in the checkout database
    @Modifying   // this annotation is used bcos we're going to be modifying the record ie going to delete the record in the database
    @Query("delete from Checkout where bookId in :book_id")         // IMP: here inside the @Query annotation , write bookId in :book_id bcos the bookId is only mentioned as column name in the Checkout Entity as well. ie if we want to delete all the checkout repositories that have the bookId as a column in the checkout database
    void deleteAllByBookId(@Param("book_id")Long bookId);
}
