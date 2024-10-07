package com.luv2code.spring_boot_library.requestmodels;

import lombok.Data;

import java.util.Optional;

@Data // this is for the lombok annotation
public class ReviewRequest {

    // This ReviewRequest class is going to be the object that the client side react frontend is going to send to us as the backend object

    private double rating;

    private Long bookId;

    // the reason we're making this an optional type bcos reviewDescription is not required when the user leaving a review of the Book. reviewDescription is not compulsory user can just give a 5 star or 4 star rating without the description.
    private Optional<String> reviewDescription;

}
