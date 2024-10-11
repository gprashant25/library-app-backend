package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dao.PaymentRepository;
import com.luv2code.spring_boot_library.entity.Payment;
import com.luv2code.spring_boot_library.requestmodels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;

    // below Injected the secret key from the application.properties file
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}") String secretKey){
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;     // Initialize Stripe API with secret key
    }

    // Creating a paymentIntent service, which is going to call our Stripe API functionality.
    // Stripe makes this extremely easy by using the built-in functionality that we get from installing our Maven stripe dependency
    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {

        // here we need to add the payment method types that we're allowing STRIPE to do, and we are going to be allowing card payments
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        // here we need to create a Map of parameters which is going to have the key of the amount, currency and our payment method types with the value of the amount, currency and payment method type.
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfoRequest.getAmount());
        params.put("currency", paymentInfoRequest.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        // Please Note: here PaymentIntent is from the Stripe API which is PCI compliant. below code functionality is all handled from Stripe
        return PaymentIntent.create(params);

    }


    // here this is going to be our Stripe payment, which changes the user's payment amount to zero after a successful STRIPE transaction.
    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {

        // so here we are locating our user in returning the data from our PaymentRepository
        Payment payment = paymentRepository.findByUserEmail(userEmail);

        if(payment == null) {
            throw new Exception("Payment information is missing");
        }

        // After a successful Stripe transaction set the amount to zero
        payment.setAmount(00.00);

        paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
