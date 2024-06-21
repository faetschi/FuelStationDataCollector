package at.fhtw.pdfgenerator.service;

import at.fhtw.pdfgenerator.config.RabbitMQConfig;
import at.fhtw.pdfgenerator.entity.CustomerEntity;
import at.fhtw.pdfgenerator.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PDFGeneratorService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @RabbitListener(queues = RabbitMQConfig.PDF_GENERATOR_QUEUE)
    public void receiveMessage(String messageContent) throws Exception {
        try {
            // Convert the messageContent to a Map<String, Object>
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> message = mapper.readValue(messageContent, Map.class);

            // Extract data and customer ID from the message
            Map<String, Double> data = (Map<String, Double>) message.get("data");
            int customerId = (int) message.get("customerId");

            // Fetch customer's first and last name from the database
            CustomerEntity customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
            String firstName = customer.getFirstName();
            String lastName = customer.getLastName();

            // Round kWh values to 2 decimal places
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                double roundedValue = Math.round(entry.getValue() * 100.0) / 100.0;
                entry.setValue(roundedValue);
            }

            System.out.println("Generating invoice for " + firstName + " " + lastName + " with data: " + data);
            // Generate the invoice with customerId
            invoiceGenerator.createInvoice(data, firstName, lastName, customerId);
        } catch (RuntimeException e) {
            // Log the exception and do not rethrow
            System.err.println("Failed to process message: " + e.getMessage());

        }

    }
}
