package at.fhtw.pdfgenerator.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class InvoiceGenerator {

    private String fileStoragePath = ".." + File.separator + "FileStorage";
    private int invoiceCounter = 0;
    public void createInvoice(Map<String, Double> data, String firstName, String lastName, int customerId) {
        try {
            // Specify the folder where the PDF should be saved
            File directory = new File(fileStoragePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            invoiceCounter++;

            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(directory.toString() + File.separator + "invoice_" + customerId + "_" + invoiceCounter + ".pdf");

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            Document document = new Document(pdf, PageSize.A4);

            // Add a paragraph with proper header and text
            Paragraph header = new Paragraph("Invoice for " + firstName + " " + lastName + ", Invoice Number: " + invoiceCounter)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(14)
                    .setBold();
            document.add(header);

            // Add a table for the station data
            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            table.addHeaderCell(getHeaderCell("Station"));
            table.addHeaderCell(getHeaderCell("kWh"));

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                table.addCell(entry.getKey());
                table.addCell(entry.getValue().toString());
            }

            document.add(table);

            // Close document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Cell getHeaderCell(String text) throws IOException {
        return new Cell().add(new Paragraph(text)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(12));
    }
}