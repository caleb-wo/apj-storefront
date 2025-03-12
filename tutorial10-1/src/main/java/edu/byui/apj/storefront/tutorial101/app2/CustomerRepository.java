package edu.byui.apj.storefront.tutorial101.app2;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);

    Customer findById(long id);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> findByFirstNameOrLastNameContainingIgnoreCase(@Param("name") String name);
}