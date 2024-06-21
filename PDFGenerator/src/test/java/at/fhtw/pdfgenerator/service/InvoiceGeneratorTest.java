package at.fhtw.pdfgenerator.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceGeneratorTest {

    @InjectMocks
    private InvoiceGenerator invoiceGenerator;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceGenerator.fileStoragePath = tempDir.getAbsolutePath();
    }

    @Test
    void testCreateInvoice() throws IOException {
        Map<String, Double> data = new HashMap<>();
        data.put("Station A", 10.0);
        data.put("Station B", 20.0);

        invoiceGenerator.createInvoice(data, "John", "Doe", 1);

        File[] files = tempDir.listFiles();
        assertNotNull(files);
        assertEquals(1, files.length);

        File pdfFile = files[0];
        assertTrue(pdfFile.getName().startsWith("invoice_1_"));

        try (PdfReader reader = new PdfReader(pdfFile.getPath());
             PdfDocument pdfDoc = new PdfDocument(reader)) {
            assertEquals(1, pdfDoc.getNumberOfPages());

        }
    }
}
