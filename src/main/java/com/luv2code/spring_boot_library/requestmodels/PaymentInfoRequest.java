package com.luv2code.spring_boot_library.requestmodels;

import lombok.Data;

@Data
public class PaymentInfoRequest {

    private int amount;

    private String currency;

    // here this is going to allow us to be able to send emails when a user does a payment
    private String receiptEmail;
}
