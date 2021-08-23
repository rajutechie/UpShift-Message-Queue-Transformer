package com.cognizant.cde.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilityServiceTest {

    private FileUtilityService fileUtilityServiceUnderTest;

    @BeforeEach
    void setUp() {
        fileUtilityServiceUnderTest = new FileUtilityService();
    }

    @Test
    void testCopyDir() throws Exception {
        // Setup

        // Run the test
        fileUtilityServiceUnderTest.copyDir("sourceDirectoryLocation", "destinationDirectoryLocation");

        // Verify the results
    }

    @Test
    void testCopyDir_ThrowsIOException() {
        // Setup

        // Run the test
        assertThrows(IOException.class, () -> fileUtilityServiceUnderTest.copyDir("sourceDirectoryLocation", "destinationDirectoryLocation"));
    }
}
