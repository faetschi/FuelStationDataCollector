package at.fhtw.springbootapp.controller;

import at.fhtw.springbootapp.RabbitMQSender;
import at.fhtw.springbootapp.entity.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @PostMapping("/{customerId}")
    public ResponseEntity<String> startDataCollection(@PathVariable String customerId) {
        rabbitMQSender.sendStartMessage(customerId);
        return new ResponseEntity<>("Invoice generation started for customer " + customerId, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getInvoice(@PathVariable String customerId) {
        InvoiceEntity invoice = new InvoiceEntity();
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(invoice);
        }
    }
}
