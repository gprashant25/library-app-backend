package com.luv2code.spring_boot_library.controller;

import com.luv2code.spring_boot_library.requestmodels.ReviewRequest;
import com.luv2code.spring_boot_library.service.ReviewService;
import com.luv2code.spring_boot_library.utils.ExtractJWT;
import org.springframework.hateoas.mediatype.alps.Ext;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    // Below adding the Review endpoints or API's to our Spring boot backend

    @PostMapping("/secure")    // here we will be saving the new review
    public void postReview(@RequestHeader(value = "Authorization")String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception{

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if(userEmail == null){
            throw new Exception("User Email is missing");
        }

        reviewService.postReview(userEmail, reviewRequest);

    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization")String token,
                                    @RequestParam Long bookId) throws Exception{

        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");

        if(userEmail == null){
            throw new Exception("User Email is missing");
        }

        return reviewService.userReviewListed(userEmail, bookId);
    }

}
