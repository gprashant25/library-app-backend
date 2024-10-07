package com.luv2code.spring_boot_library.responsemodels;

import com.luv2code.spring_boot_library.entity.Book;
import lombok.Data;

@Data  // adding the Lombok annotation
public class ShelfCurrentLoansResponse {

    private Book book;
    private int daysLeft;

    public ShelfCurrentLoansResponse(Book book, int daysLeft){
        this.book = book;
        this.daysLeft = daysLeft;
    }
}
