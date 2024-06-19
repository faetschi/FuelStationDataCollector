package at.fhtw.springbootapp.controller;

import at.fhtw.springbootapp.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    private static final String ROOT_DIRECTORY = new File("").getAbsolutePath(); // Get current working directory
    @PostMapping("/{customerId}")
    public ResponseEntity<String> startDataCollection(@PathVariable String customerId) {
        rabbitMQSender.sendStartMessage(customerId);
        return new ResponseEntity<>("Invoice generation started for customer " + customerId, HttpStatus.OK);
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<String> getInvoice(@PathVariable String customerId) {
        try {
            String fileStoragePath = ROOT_DIRECTORY + File.separator + ".." + File.separator + "FileStorage";
            int highestInvoiceCounter = findHighestInvoiceCounter(fileStoragePath, customerId);

            if (highestInvoiceCounter == -1) {
                return ResponseEntity.notFound().build();
            }

            String fileName = "invoice_" + customerId + "_" + highestInvoiceCounter + ".pdf";
            Path filePath = Paths.get(fileStoragePath).resolve(fileName);

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String downloadLink = resource.getURI().toString();
                return ResponseEntity.ok(downloadLink);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private int findHighestInvoiceCounter(String fileStoragePath, String customerId) {
        try {
            // List all files in the FileStorage directory
            Path directory = Paths.get(fileStoragePath);
            return Files.list(directory)
                    .filter(file -> file.getFileName().toString().startsWith("invoice_" + customerId + "_"))
                    .map(file -> {
                        String fileName = file.getFileName().toString();
                        int startIndex = fileName.lastIndexOf("_") + 1;
                        int endIndex = fileName.lastIndexOf(".");
                        return Integer.parseInt(fileName.substring(startIndex, endIndex));
                    })
                    .max(Comparator.naturalOrder())
                    .orElse(-1); // Return -1 if no invoices found
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
