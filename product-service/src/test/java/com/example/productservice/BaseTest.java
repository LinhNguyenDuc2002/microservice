package com.example.productservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    /**
     * Clean the data after test
     */
    @AfterEach
    protected void setupAfterEach() {
        cleanAfterEach();
    }

    /**
     * Clean the data before test
     */
    @BeforeEach
    protected void setupBeforeEach() {
        cleanAfterEach();
    }

    /**
     * Clean test data after each test
     */
    protected abstract void cleanAfterEach();

    /**
     * Clean test data before each test
     */
    protected abstract void cleanBeforeEach();
}
