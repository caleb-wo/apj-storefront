package edu.byui.apj.storefront.tutorial102.accessingDataMongoDb;

import java.util.List;

import edu.byui.apj.storefront.tutorial102.accessingDataMongoDb.Customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);

}