package edu.byui.apj.storefront.tutorial102;

import edu.byui.apj.storefront.tutorial102.accessingMongoDbRest.AccessingMongoDbRestApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Tutorial102Application {

    public static void main(String[] args) {
        SpringApplication.run(AccessingMongoDbRestApplication.class, args);
    }

}
