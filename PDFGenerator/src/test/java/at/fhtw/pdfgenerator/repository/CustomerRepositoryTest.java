package at.fhtw.pdfgenerator.repository;

import at.fhtw.pdfgenerator.entity.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindById() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        customerRepository.save(customer);

        Optional<CustomerEntity> foundCustomer = customerRepository.findById(1);
        assertTrue(foundCustomer.isPresent());
    }
}
