package com.edu.hanu.controller;

import com.edu.hanu.service.UserPDFExporter;
import com.edu.hanu.repository.UserRepository;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Controller
public class PDFController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

//        List<User>  listUsers = userRepository.findAll();

        UserPDFExporter exporter = new UserPDFExporter();
        exporter.export(response);
    }
}
