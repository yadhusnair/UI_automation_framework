package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteRunner {
    private static final Logger logger = LoggerFactory.getLogger(SuiteRunner.class);
    private TestSuiteManager suiteManager;
    private TomlConfigManager configManager;

    public SuiteRunner() {
        this.suiteManager = TestSuiteManager.getInstance();
        this.configManager = TomlConfigManager.getInstance();
    }

    /**
     * Run a specific test suite from config.toml
     * 
     * @param suiteName Name of the suite to run
     * @return true if suite ran successfully, false otherwise
     */
    public boolean runSuite(String suiteName) {
        String[] tests = suiteManager.getSuite(suiteName);
        
        if (tests.length == 0) {
            logger.error("Suite '{}' not found or is empty", suiteName);
            return false;
        }

        logger.info("========================================");
        logger.info("Starting execution of suite: {}", suiteName);
        logger.info("Total tests to execute: {}", tests.length);
        for (String test : tests) {
            logger.info("  - {}", test.trim());
        }
        logger.info("========================================");

        try {
            TestNG testng = new TestNG();
            testng.setVerbose(2);
            testng.addListener(new org.example.listeners.TestListener());
            
            XmlSuite xmlSuite = createXmlSuite(suiteName, tests);
            testng.setXmlSuites(List.of(xmlSuite));
            testng.run();

            logger.info("Suite '{}' execution completed", suiteName);
            return testng.hasFailure() == false;
        } catch (Exception e) {
            logger.error("Error running suite: {}", suiteName, e);
            return false;
        }
    }

    /**
     * Run multiple suites sequentially
     * 
     * @param suiteNames Array of suite names to run
     * @return true if all suites ran without failures
     */
    public boolean runMultipleSuites(String... suiteNames) {
        boolean allPassed = true;
        
        for (String suiteName : suiteNames) {
            logger.info("Running suite: {}", suiteName);
            if (!runSuite(suiteName)) {
                allPassed = false;
                logger.warn("Suite '{}' had failures", suiteName);
            }
        }
        
        return allPassed;
    }

    /**
     * Run all configured suites
     * 
     * @return true if all suites ran without failures
     */
    public boolean runAllSuites() {
        String[] allSuites = suiteManager.getAvailableSuites();
        return runMultipleSuites(allSuites);
    }

    /**
     * Create an XML suite from test names
     */
    private XmlSuite createXmlSuite(String suiteName, String[] tests) {
        XmlSuite xmlSuite = new XmlSuite();
        xmlSuite.setName(suiteName);
        xmlSuite.setThreadCount(1);

        XmlTest xmlTest = new XmlTest(xmlSuite);
        xmlTest.setName(suiteName + "-Test");

        List<XmlClass> xmlClasses = new ArrayList<>();
        String lastClassName = null;
        
        for (String test : tests) {
            String[] parts = test.trim().split("\\.");
            if (parts.length >= 2) {
                String className = test.substring(0, test.lastIndexOf('.'));
                
                // Only add class once
                if (!className.equals(lastClassName)) {
                    xmlClasses.add(new XmlClass(className));
                    lastClassName = className;
                }
            }
        }

        xmlTest.setXmlClasses(xmlClasses);
        return xmlSuite;
    }

    /**
     * List all available suites
     */
    public void listAvailableSuites() {
        logger.info("========================================");
        logger.info("Available Test Suites:");
        logger.info("========================================");
        
        String[] suites = suiteManager.getAvailableSuites();
        
        if (suites.length == 0) {
            logger.warn("No test suites defined in config.toml");
            return;
        }
        
        for (String suite : suites) {
            String[] tests = suiteManager.getSuite(suite);
            logger.info("Suite: {} ({} test(s))", suite, tests.length);
            for (String test : tests) {
                logger.info("  - {}", test.trim());
            }
        }
        
        logger.info("========================================");
    }

    /**
     * Run suite and send email report
     * 
     * @param suiteName Suite to run
     * @param sendEmail Whether to send email report
     */
    public void runSuiteWithReport(String suiteName, boolean sendEmail) {
        long startTime = System.currentTimeMillis();
        
        logger.info("Starting test suite execution: {}", suiteName);
        boolean success = runSuite(suiteName);
        
        long duration = System.currentTimeMillis() - startTime;
        
        if (sendEmail && configManager.isEmailEnabled()) {
            String summary = success ? "All tests passed" : "Some tests failed";
            EmailReporter emailReporter = new EmailReporter();
            emailReporter.sendReport(suiteName, "", summary + " (Duration: " + duration + "ms)");
        }
        
        if (success) {
            logger.info("✓ Suite '{}' completed successfully in {}ms", suiteName, duration);
        } else {
            logger.error("✗ Suite '{}' had failures after {}ms", suiteName, duration);
        }
    }

    /**
     * Main entry point for running suites from command line
     */
    public static void main(String[] args) {
        SuiteRunner runner = new SuiteRunner();
        
        if (args.length == 0) {
            logger.info("Usage: java SuiteRunner [suite-name] [--list] [--all]");
            logger.info("Examples:");
            logger.info("  java SuiteRunner smoke          - Run smoke suite");
            logger.info("  java SuiteRunner regression     - Run regression suite");
            logger.info("  java SuiteRunner --list         - List all suites");
            logger.info("  java SuiteRunner --all          - Run all suites");
            
            runner.listAvailableSuites();
            return;
        }
        
        String command = args[0];
        
        if ("--list".equalsIgnoreCase(command)) {
            runner.listAvailableSuites();
        } else if ("--all".equalsIgnoreCase(command)) {
            logger.info("Running all configured suites...");
            runner.runAllSuites();
        } else {
            // Run specific suite
            runner.runSuiteWithReport(command, true);
        }
    }
}
