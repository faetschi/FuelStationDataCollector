package at.fhtw.springbootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @PostMapping("/{customerId}")
    public void startDataCollection(@PathVariable String customerId) {
        rabbitMQSender.sendStartMessage(customerId);
    }
}
