package com.cognizant.cde.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IdentifyRabbitQueueServiceTest {

    @Mock
    private FileUtilityService mockFileUtilityService;

    private IdentifyRabbitQueueService identifyRabbitQueueServiceUnderTest;

    @BeforeEach
    void setUp() {
        identifyRabbitQueueServiceUnderTest = new IdentifyRabbitQueueService(mockFileUtilityService);
    }

    @Test
    void testProcessMQMigrate() throws Exception {
        // Setup

        // Run the test
        identifyRabbitQueueServiceUnderTest.processMQMigrate("projectRootPath", "projectDestinationPath");

        // Verify the results
        verify(mockFileUtilityService).copyDir("sourceDirectoryLocation", "destinationDirectoryLocation");
    }

    @Test
    void testProcessMQMigrate_FileUtilityServiceThrowsIOException() throws Exception {
        // Setup
        doThrow(IOException.class).when(mockFileUtilityService).copyDir("sourceDirectoryLocation", "destinationDirectoryLocation");

        // Run the test
        assertThrows(IOException.class, () -> identifyRabbitQueueServiceUnderTest.processMQMigrate("projectRootPath", "projectDestinationPath"));
    }
}
