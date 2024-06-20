package at.fhtw.pdfgenerator.service;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvoiceGeneratorTest {

    @Test
    public void testCreateInvoice() throws IOException {
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

        Map<String, Double> data = new HashMap<>();
        data.put("Station1", 10.0);
        data.put("Station2", 20.5);

        String firstName = "John";
        String lastName = "Doe";
        int customerId = 1;

        invoiceGenerator.createInvoice(data, firstName, lastName, customerId);

        File directory = new File(".." + File.separator + "FileStorage");
        File file = new File(directory, "invoice_" + customerId + "_1.pdf");

        assertTrue(file.exists());


        file.delete();
        directory.delete();
    }
}
