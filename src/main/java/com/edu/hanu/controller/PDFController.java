package com.edu.hanu.controller;

import com.edu.hanu.model.Ticket;
import com.edu.hanu.repository.TicketRepository;
import com.edu.hanu.service.UserPDFExporter;
import com.edu.hanu.repository.UserRepository;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class PDFController {
    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/cart/export/pdf/{id}")
    public void exportToPDF(HttpServletResponse response, @PathVariable(value = "id") long id) throws DocumentException, IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        List<Ticket> listTicket = ticketRepository.findById(id);

        UserPDFExporter exporter = new UserPDFExporter(listTicket);
        exporter.export(response);
    }
}
