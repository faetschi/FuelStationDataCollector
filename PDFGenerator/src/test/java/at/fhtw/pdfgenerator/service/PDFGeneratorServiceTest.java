package at.fhtw.pdfgenerator.service;

import at.fhtw.pdfgenerator.entity.CustomerEntity;
import at.fhtw.pdfgenerator.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PDFGeneratorServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private InvoiceGenerator invoiceGenerator;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Test
    public void testReceiveMessage() throws Exception {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        when(customerRepository.findById(1)).thenReturn(java.util.Optional.of(customer));

        Map<String, Object> message = new HashMap<>();
        message.put("customerId", 1);
        Map<String, Double> data = new HashMap<>();
        data.put("Station1", 10.0);
        data.put("Station2", 20.12345);
        message.put("data", data);

        ObjectMapper mapper = new ObjectMapper();
        String messageContent = mapper.writeValueAsString(message);

        pdfGeneratorService.receiveMessage(messageContent);

        ArgumentCaptor<Map<String, Double>> dataCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> firstNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> lastNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> customerIdCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(invoiceGenerator, times(1)).createInvoice(dataCaptor.capture(), firstNameCaptor.capture(), lastNameCaptor.capture(), customerIdCaptor.capture());

        assertEquals("John", firstNameCaptor.getValue());
        assertEquals("Doe", lastNameCaptor.getValue());
        assertEquals(1, customerIdCaptor.getValue().intValue());

        Map<String, Double> roundedData = dataCaptor.getValue();
        assertEquals(10.0, roundedData.get("Station1"));
        assertEquals(20.12, roundedData.get("Station2"));
    }
}
