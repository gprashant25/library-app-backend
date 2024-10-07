package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dao.BookRepository;
import com.luv2code.spring_boot_library.dao.ReviewRepository;
import com.luv2code.spring_boot_library.entity.Review;
import com.luv2code.spring_boot_library.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }


    // creating a POST Review function
    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {

        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if(validateReview != null){
            throw new Exception("Review already created");
        }

        Review review = new Review();

        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);

        // note: as reviewDescription is optional so if its present then we want to do is move over that optional into our review. however optional will copy over strings.
        // here we're using the java map functionality to allow us to copy over all the data from the optional object to our String.
        // if this request has a get reviewDescription then we want to change that to a string and save it to the database
        if(reviewRequest.getReviewDescription().isPresent()){
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    Object::toString).orElse(null)
            );
        }
        review.setDate(Date.valueOf(LocalDate.now()));

        reviewRepository.save(review);

    }

    public Boolean userReviewListed(String userEmail, Long bookId){

        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateReview != null){
            return true;
        } else {
            return false;
        }
    }

}
