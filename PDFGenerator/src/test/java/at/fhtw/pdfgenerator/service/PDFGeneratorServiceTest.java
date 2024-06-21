package at.fhtw.pdfgenerator.service;

import at.fhtw.pdfgenerator.config.RabbitMQConfig;
import at.fhtw.pdfgenerator.entity.CustomerEntity;
import at.fhtw.pdfgenerator.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PDFGeneratorServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InvoiceGenerator invoiceGenerator;

    @InjectMocks
    private PDFGeneratorService pdfGeneratorService;

    @BeforeEach
    void setUp() {
        reset(customerRepository, invoiceGenerator);
    }

    @Test
    void testReceiveMessage_ValidCustomer() throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("data", Map.of("Station A", 10.0, "Station B", 20.0));
        message.put("customerId", 1);

        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        ObjectMapper mapper = new ObjectMapper();
        String messageContent = mapper.writeValueAsString(message);

        pdfGeneratorService.receiveMessage(messageContent);

        verify(invoiceGenerator).createInvoice(anyMap(), eq("John"), eq("Doe"), eq(1));
    }

    @Test
    void testReceiveMessage_InvalidCustomer() throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("data", Map.of("Station A", 10.0, "Station B", 20.0));
        message.put("customerId", 1);

        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        ObjectMapper mapper = new ObjectMapper();
        String messageContent = mapper.writeValueAsString(message);

        pdfGeneratorService.receiveMessage(messageContent);

        verify(invoiceGenerator, never()).createInvoice(anyMap(), anyString(), anyString(), anyInt());
    }
}
