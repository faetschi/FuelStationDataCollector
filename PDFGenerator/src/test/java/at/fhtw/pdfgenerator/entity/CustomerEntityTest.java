package at.fhtw.pdfgenerator.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerEntityTest {

    @Test
    public void testCustomerEntity() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        assertNotNull(customer);
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
    }
}
