package edu.byui.apj.storefront.tutorial101;

import edu.byui.apj.storefront.tutorial101.app1.AccessingDataRestApplication;
import edu.byui.apj.storefront.tutorial101.app2.AccessingDataJpaApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Tutorial101Application {

	public static void main(String[] args) {
		switch (args[0]) {
			case "1":
				SpringApplication.run(AccessingDataRestApplication.class, args);
				break;
			case "2":
				SpringApplication.run(AccessingDataJpaApplication.class, args);
				break;
			default:
				System.out.println("Invalid arguments.");
		}
	}

}
