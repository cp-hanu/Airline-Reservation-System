package com.edu.hanu.config;

import com.edu.hanu.model.Ticket;
import com.edu.hanu.model.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserPDFExporter {

    private List<Ticket> listTicket;

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

//        row 1
        PdfPTable tableHeader = new PdfPTable(2);
        tableHeader.setWidthPercentage(100f);
        tableHeader.setWidths(new float[]{5f,5f});
        tableHeader.setSpacingBefore(15);

        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(new Color(54,189,194));
        cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellHeader.setPadding(15);
        cellHeader.setBorder(0);
        cellHeader.setPhrase(new Phrase("BOARDING PASS",font));
        tableHeader.addCell(cellHeader);
//        for(Ticket tick : listTicket){
//            cellHeader.setPhrase(new Phrase(tick.getFlight().getAirline().getFullName(),font));
//            tableHeader.addCell(cellHeader);
//        }
        cellHeader.setPhrase(new Phrase("VietNam Airlines",font));
        tableHeader.addCell(cellHeader);

        //row 2
        PdfPTable tableContent = new PdfPTable(3);
        tableContent.setWidthPercentage(100f);
        tableContent.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        tableContent.setWidths(new float[]{3f,4f,3f});
        tableContent.setSpacingBefore(15);
        tableContent.getDefaultCell().setBorder(0);
        PdfPCell cellContent = new PdfPCell();
        cellContent.setBorder(Rectangle.NO_BORDER);
        cellContent.setPadding(8);
        cellContent.setBorder(0);
        cellContent.setPhrase(new Phrase("FLIGHT",font));
        tableContent.addCell(cellContent);
        cellContent.setPhrase(new Phrase("BOARDING TIME",font));
        tableContent.addCell(cellContent);
        cellContent.setPhrase(new Phrase("SEAT",font));
        tableContent.addCell(cellContent);


        //row 3
        PdfPTable tableName = new PdfPTable(3);
        tableName.setWidthPercentage(100f);
        tableName.setWidths(new float[]{4.5f,3f,3f});
        tableName.getDefaultCell().setBorder(0);
        tableName.setSpacingBefore(25);
        PdfPCell cellName = new PdfPCell();
        cellName.setPadding(15);
        cellName.setBorder(Rectangle.NO_BORDER);

        cellName.setPhrase(new Phrase("PASSENGER NAME",font));
        tableName.addCell(cellName);
        cellName.setPhrase(new Phrase("FROM",font));
        tableName.addCell(cellName);
        cellName.setPhrase(new Phrase("TO",font));
        tableName.addCell(cellName);

        //row-4
        PdfPTable tableFooter = new PdfPTable(2);
        tableFooter.setWidthPercentage(100f);
        tableFooter.setWidths(new float[]{3.5f,3.5f});
        tableFooter.setSpacingBefore(15);
        tableFooter.getDefaultCell().setBorder(0);

        PdfPCell cellFooter = new PdfPCell();
        cellFooter.setPadding(8);
        cellFooter.setBorder(Rectangle.NO_BORDER);
        cellFooter.setPhrase(new Phrase("Date",font));
        tableFooter.addCell(cellFooter);
        cellFooter.setPhrase(new Phrase("Class",font));
        tableFooter.addCell(cellFooter);

//        for(Ticket tick : listTicket){
//            tableHeader.addCell(tick.getFlight().getPlane().getName());
//            tableHeader.addCell(String.valueOf(tick.getFlight().getDepartureTime()));
//            tableHeader.addCell(String.valueOf(tick.getFlightSeatPrice().getSeat().getRow())
//            + tick.getFlightSeatPrice().getSeat().getCol());
//
//            tableName.addCell(tick.getFirstName() + tick.getLastName());
//            tableName.addCell(String.valueOf(tick.getFlight().getFromAirport()));
//            tableName.addCell(String.valueOf(tick.getFlight().getToAirport()));
//
//            tableFooter.addCell(String.valueOf(tick.getFlight().getArrivalDate()));
//            tableFooter.addCell(tick.getFlightSeatPrice().getSeat().getType());
//        }

        tableContent.addCell("A320");
        tableContent.addCell(String.valueOf("10:20"));
        tableContent.addCell("11A");
        tableName.addCell("John Doe");
        tableName.addCell("Ha Noi");
        tableName.addCell("Ho Chi Minh");
        tableFooter.addCell(String.valueOf("04/29/2022"));
        tableFooter.addCell("Economy");

        document.add(tableHeader);
        document.add(tableContent);
        document.add(tableName);
        document.add(tableFooter);
        document.close();
    }
}
