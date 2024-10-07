package com.luv2code.spring_boot_library.requestmodels;

import lombok.Data;

// here we're going to be creating the endpoints that allows us to be able to add new Book to our SpringBoot application.
// here we need to do to be able to add a new Book is to be able to create a object.

@Data
public class AddBookRequest {

    private String title;

    private String author;

    private String description;

    private int copies;

    private String category;

    private String img;


}
