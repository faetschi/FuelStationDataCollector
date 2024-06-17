package at.fhtw.pdfgenerator.repository;

import at.fhtw.pdfgenerator.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}