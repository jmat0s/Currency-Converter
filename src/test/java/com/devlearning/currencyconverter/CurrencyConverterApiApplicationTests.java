package com.devlearning.currencyconverter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test class to verify the Spring Application Context loading.
 * <p>
 * This class serves as a "Sanity Check" (or Smoke Test). It ensures that the application 
 * configuration, bean wiring, and property files are correct enough for the app to start up.
 */
@SpringBootTest
class CurrencyConverterApiApplicationTests {

    /**
     * Basic context load test.
     * <p>
     * This test method is empty because the assertion is implicit:
     * If the Spring Context fails to initialize (e.g., due to missing beans, 
     * circular dependencies, or bad configuration), the test runner will throw 
     * an exception and fail the test before even entering this method body.
     */
    @Test
    void contextLoads() {
        // No explicit assertion needed. 
        // If the context loads successfully, the test passes.
    }

}