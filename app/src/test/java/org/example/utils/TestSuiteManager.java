package org.example.utils;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSuiteManager {
    private static final Logger logger = LoggerFactory.getLogger(TestSuiteManager.class);
    private static TestSuiteManager instance;
    private TomlConfigManager configManager;
    private Map<String, String[]> suiteCache;

    private TestSuiteManager() {
        this.configManager = TomlConfigManager.getInstance();
        this.suiteCache = new HashMap<>();
        loadSuites();
    }

    public static synchronized TestSuiteManager getInstance() {
        if (instance == null) {
            instance = new TestSuiteManager();
        }
        return instance;
    }

    private void loadSuites() {
        // Suites are loaded on-demand from config
        String[] availableSuites = configManager.getAvailableSuites();
        logger.info("Test suites loaded from config.toml: {}", String.join(", ", availableSuites));
    }

    /**
     * Get all defined test suites
     * 
     * @return Map of suite name to test methods
     */
    public Map<String, String[]> getAllSuites() {
        if (suiteCache.isEmpty()) {
            String[] suiteNames = configManager.getAvailableSuites();
            for (String suiteName : suiteNames) {
                String[] tests = configManager.getSuite(suiteName);
                suiteCache.put(suiteName, tests);
            }
        }
        return suiteCache;
    }

    /**
     * Get tests for a specific suite
     * 
     * @param suiteName Name of the test suite
     * @return Array of test methods
     */
    public String[] getSuite(String suiteName) {
        // Check cache first
        if (suiteCache.containsKey(suiteName)) {
            return suiteCache.get(suiteName);
        }

        // Load from config
        String[] tests = configManager.getSuite(suiteName);
        suiteCache.put(suiteName, tests);
        return tests;
    }

    /**
     * Get available suite names
     * 
     * @return Array of suite names
     */
    public String[] getAvailableSuites() {
        return configManager.getAvailableSuites();
    }

    /**
     * Check if a suite exists
     * 
     * @param suiteName Name of the suite
     * @return true if suite exists
     */
    public boolean suiteExists(String suiteName) {
        return getSuite(suiteName).length > 0;
    }

    /**
     * Print available suites to logs
     */
    public void logAvailableSuites() {
        logger.info("Available Test Suites:");
        for (String suite : getAvailableSuites()) {
            String[] tests = getSuite(suite);
            logger.info("  - {}: {} test(s)", suite, tests.length);
            for (String test : tests) {
                logger.debug("    * {}", test.trim());
            }
        }
    }

    /**
     * Add a test to a suite at runtime
     * 
     * @param suiteName Suite name
     * @param testMethod Test method to add
     */
    public void addTestToSuite(String suiteName, String testMethod) {
        String[] currentTests = getSuite(suiteName);
        String[] newTests = new String[currentTests.length + 1];
        System.arraycopy(currentTests, 0, newTests, 0, currentTests.length);
        newTests[currentTests.length] = testMethod;
        suiteCache.put(suiteName, newTests);
        logger.info("Added test '{}' to suite '{}'", testMethod, suiteName);
    }

    /**
     * Remove a test from a suite at runtime
     * 
     * @param suiteName Suite name
     * @param testMethod Test method to remove
     */
    public void removeTestFromSuite(String suiteName, String testMethod) {
        String[] currentTests = getSuite(suiteName);
        String[] newTests = new String[currentTests.length - 1];
        int idx = 0;
        for (String test : currentTests) {
            if (!test.trim().equals(testMethod.trim())) {
                newTests[idx++] = test;
            }
        }
        suiteCache.put(suiteName, newTests);
        logger.info("Removed test '{}' from suite '{}'", testMethod, suiteName);
    }

    /**
     * Create a new suite at runtime
     * 
     * @param suiteName Name for the new suite
     * @param tests Array of tests for the suite
     */
    public void createSuite(String suiteName, String[] tests) {
        suiteCache.put(suiteName, tests);
        logger.info("Created new suite '{}' with {} test(s)", suiteName, tests.length);
    }
}
