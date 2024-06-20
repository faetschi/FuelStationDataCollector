package at.fhtw.springbootapp;

import at.fhtw.springbootapp.controller.InvoiceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SpringBootAppApplicationTests {

    private MockMvc mockMvc;

    @InjectMocks
    private InvoiceController invoiceController;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testPostEndpoint() throws Exception {
        String customerId = "1";
        mockMvc.perform(post("/invoices/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Invoice generation started for customer " + customerId));

        verify(rabbitMQSender, times(1)).sendStartMessage(customerId);
    }

    @Test
    public void testGetEndpoint() throws Exception {
        String customerId = "1";
        String rootDirectory = System.getProperty("user.dir").replace("\\", "/").replace(" ", "%20");

        // Create a spy of the InvoiceController
        InvoiceController invoiceControllerSpy = spy(invoiceController);

        // Mocking the behavior of findHighestInvoiceCounter method in the spy
        doReturn(2).when(invoiceControllerSpy).findHighestInvoiceCounter(anyString(), anyString());

        int highestInvoiceNumber = invoiceControllerSpy.findHighestInvoiceCounter(rootDirectory + "/../FileStorage", customerId);

        // Adjust the expectedResponse
        String expectedResponse = "file:///" + rootDirectory + "/../FileStorage/invoice_" + customerId +"_" + highestInvoiceNumber + ".pdf";

        // Perform the GET request
        mockMvc.perform(get("/invoices/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }


}
