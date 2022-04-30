package com.edu.hanu.service;

import com.edu.hanu.model.PaypalPaymentIntent;
import com.edu.hanu.model.PaypalPaymentMethod;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;

    public Payment createPayment(Double totalPriceFlightBooked, String internationalCurrency,
                                 PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent,
                                 String description, String cancelUrl,
                                 String successUrl) throws PayPalRESTException{
//        amount of flight booked
        Amount amountFlightBooked = new Amount();
        amountFlightBooked.setCurrency(internationalCurrency);
        totalPriceFlightBooked = new BigDecimal(totalPriceFlightBooked).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amountFlightBooked.setTotal(String.format("%.0f",totalPriceFlightBooked));


//        transaction flight booked
        Transaction tranFlight = new Transaction();
        tranFlight.setDescription(description);
        tranFlight.setAmount(amountFlightBooked);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(tranFlight);

//        payer transaction
        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

//        payment
        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(listTransaction);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);//after payment fail or success
//        apiContext.setMaskRequestId(true);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId,String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext,paymentExecution);
    }
}
