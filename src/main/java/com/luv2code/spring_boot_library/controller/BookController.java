package com.luv2code.spring_boot_library.controller;

import com.luv2code.spring_boot_library.entity.Book;
import com.luv2code.spring_boot_library.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.spring_boot_library.service.BookService;
import com.luv2code.spring_boot_library.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("https://localhost:3000")   // this means that our React frontend application will be able to call this BookController without causing the CORS error
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // IMPORTANT content : JWT TOKEN
    // PLEASE NOTE: here we need to be able to extract the JWT from the frontend client-side Okta configuration code written to pull out the information of users inside of each JWT
    // so we're using @RequestHeader(value = "Authorization)String token annotation. here it has a key of authorization and we want to pull the value out into aour variable called token.
    // And thats exactly how we're passing the access token to our Backend application and its validating with Okta automatically.

    @PutMapping("/secure/checkout")
    public Book checkoutBook (@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception{

        // String userEmail = "testuser1@email.com";

        // below we're extracting the username from the JWT token passed in the RequestHeader
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");

        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId){

      //  String userEmail = "testuser1@email.com";   //here we're manually passing the userEmail

        // below we're extracting the username from the JWT token passed in the RequestHeader
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");

        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    // IMPORTANT content : JWT TOKEN
    // PLEASE NOTE: here we need to be able to extract the JWT from the frontend client-side Okta configuration code written to pull out the information of users inside of each JWT
    // so we're using @RequestHeader(value = "Authorization)String token annotation. here it has a key of authorization and we want to pull the value out into aour variable called token.
    // And thats exactly how we're passing the access token to our Backend application and its validating with Okta automatically.

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token){

        // String userEmail = "testuser1@email.com";

        // below we're extracting the username from the JWT token passed in the RequestHeader
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");

        return bookService.currentLoansCount(userEmail);
    }


    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
            throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");

        return bookService.currentLoans(userEmail);
    }


    // Return Book service endpoint API
    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization")String token,
                           @RequestParam Long bookId) throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        bookService.returnBook(userEmail, bookId);
    }


    // Renew Loan Endpoint API
    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value = "Authorization")String token,
                          @RequestParam Long bookId) throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.renewLoan(userEmail, bookId);
    }
}
