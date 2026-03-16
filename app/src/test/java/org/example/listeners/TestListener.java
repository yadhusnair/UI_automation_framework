package org.example.listeners;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.example.utils.EmailReporter;
import org.example.utils.TomlConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, ISuiteListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);
    private EmailReporter emailReporter;
    private TomlConfigManager configManager;
    private int passedCount = 0;
    private int failedCount = 0;
    private int skippedCount = 0;
    private long suiteStartTime;

    public TestListener() {
        this.configManager = TomlConfigManager.getInstance();
        this.emailReporter = new EmailReporter();
    }

    @Override
    public void onStart(ISuite suite) {
        logger.info("===================================");
        logger.info("Test Suite Started: {}", suite.getName());
        logger.info("===================================");
        suiteStartTime = System.currentTimeMillis();
        passedCount = 0;
        failedCount = 0;
        skippedCount = 0;
    }

    @Override
    public void onFinish(ISuite suite) {
        long duration = System.currentTimeMillis() - suiteStartTime;
        logger.info("===================================");
        logger.info("Test Suite Finished: {}", suite.getName());
        logger.info("Results - Passed: {}, Failed: {}, Skipped: {}", passedCount, failedCount, skippedCount);
        logger.info("Duration: {}ms", duration);
        logger.info("===================================");

        // Send email report if enabled
        if (configManager.isEmailEnabled()) {
            sendEmailReport(suite.getName());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Context Started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Context Finished: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting Test: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedCount++;
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("✓ PASSED: {} ({}ms)",
                result.getMethod().getMethodName(),
                duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedCount++;
        String errorMsg = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "Unknown error";
        logger.error("✗ FAILED: {} - {}",
                result.getMethod().getMethodName(),
                errorMsg);

        if (result.getThrowable() != null) {
            logger.error("Stack trace:", result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedCount++;
        String reason = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "No reason provided";
        logger.warn("⊘ SKIPPED: {} - {}",
                result.getMethod().getMethodName(),
                reason);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("⚠ Test Failed Within Success Percentage: {}",
                result.getMethod().getMethodName());
    }

    /**
     * Send email report after suite execution
     */
    private void sendEmailReport(String suiteName) {
        try {
            int totalTests = passedCount + failedCount + skippedCount;
            String summary = String.format(
                    "Total: %d | Passed: %d | Failed: %d | Skipped: %d | Pass Rate: %.1f%%",
                    totalTests,
                    passedCount,
                    failedCount,
                    skippedCount,
                    totalTests > 0 ? (passedCount * 100.0 / totalTests) : 0);

            String reportPath = org.example.utils.ExtentTestListener.getReportPath();

            if (reportPath == null) {
                logger.warn("ExtentTestListener did not provide a report path, falling back to timestamp generation");
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                reportPath = configManager.getReportDirectory() +
                        "/extent-report-" + timestamp + ".html";
            }

            emailReporter.sendReport(suiteName, reportPath, summary);
            logger.info("Test report email queued for sending");
        } catch (Exception e) {
            logger.error("Error sending email report", e);
        }
    }
}
