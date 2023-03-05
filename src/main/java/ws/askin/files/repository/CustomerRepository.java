package ws.askin.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.askin.files.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {}
