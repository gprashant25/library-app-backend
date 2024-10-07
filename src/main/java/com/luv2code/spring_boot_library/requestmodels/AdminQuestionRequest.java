package com.luv2code.spring_boot_library.requestmodels;

import lombok.Data;

@Data
public class AdminQuestionRequest {

    private Long id;

    // This is going to be the object that we send from our React application to our backend application so we can update the current message with a response from the admin
    private String response;
}
