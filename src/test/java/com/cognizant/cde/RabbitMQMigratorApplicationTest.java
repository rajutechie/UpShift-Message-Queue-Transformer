package com.cognizant.cde;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RabbitMQMigratorApplicationTest {

    private RabbitMQMigratorApplication rabbitMQMigratorApplicationUnderTest;

    @BeforeEach
    void setUp() {
        rabbitMQMigratorApplicationUnderTest = new RabbitMQMigratorApplication();
    }

    @Test
    void testRun() throws Exception {
        // Setup

        // Run the test
        rabbitMQMigratorApplicationUnderTest.run("args");

        // Verify the results
    }

    @Test
    void testRun_ThrowsException() {
        // Setup

        // Run the test
        assertThrows(Exception.class, () -> rabbitMQMigratorApplicationUnderTest.run("args"));
    }

    @Test
    void testMain() {
        // Setup

        // Run the test
        RabbitMQMigratorApplication.main(new String[]{"value"});

        // Verify the results
    }
}
