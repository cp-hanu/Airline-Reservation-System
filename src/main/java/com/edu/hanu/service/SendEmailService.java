package com.edu.hanu.service;

import com.edu.hanu.model.Delay;
import com.edu.hanu.model.Flight;
import com.edu.hanu.model.Ticket;
import com.edu.hanu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    void sendMail(String title, String receiverEmail, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiverEmail);
        msg.setSubject(title);
        msg.setText(content);

        javaMailSender.send(msg);
    }

    public void confirmBookingNotification(Ticket ticket, Flight flight) {
        String content = String.format(
                "Dear Mr/mrs %s\n" +
                        "Thank you for choosing %s\n" +
                        " \n" +
                        "We are confirm your flight from %s to %s at %s %s " +
                        "\n" +
                        "Flight No: %s at %s \n" +
                        "Your PNR: %s\n" +
                        "Our professional and friendly staff are committed to ensuring your flight is both enjoyable and comfortable.\n" +
                        "\n" +
                        "Thanks & Best Regards,\n" +
                        "\n" +
                        "Shelly, %s",
                ticket.getLastName(), flight.getAirline().getFullName(),
                flight.getFromAirport().getName(), flight.getToAirport().getName(),
                flight.getDepartureTime().toString(), flight.getDepartureDate().toString(),
                flight.getFlightNo(), flight.getPlane().getBrand() + " " + flight.getPlane().getName(),
                ticket.getPnr(), flight.getAirline().getFullName()
        );
        sendMail("CONFIRMATION BOOKING", ticket.getEmail(), content);
    }

    public void delayNotification(Delay delay, Flight flight, String email) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        String departureDate = dateFormat.format(flight.getDepartureDate());
        String content = String.format("Dear Mr/Mrs " +
                "We sincerely regret to inform you that your flight %s  on %s is delayed %s\n" +
                "\n" +
                "The reason for the delay is %s \n" +
                "\n" +
                "Our team hopes we can make it right. Thank you for your patience.\n" +
                "\n" +
                "Sincerely,\n" +
                "\n" +
                "Taylor’s Designs", flight.getFlightNo(), departureDate, delay.getMinute(), delay.getReason());
        sendMail("APOLOGY FOR DELAYING", email, content);
    }
}
