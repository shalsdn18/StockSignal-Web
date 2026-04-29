package com.stocksignal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: verifies the Spring application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class StockSignalApplicationTest {

    @Test
    void contextLoads() {
        // If the context loads without exceptions, the test passes.
    }
}
