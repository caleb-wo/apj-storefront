package edu.byui.apj.storefront.tutorial101;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Tutorial101Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class Tutorial101ApplicationTests {
	@Test
	void contextLoads() {}
}
