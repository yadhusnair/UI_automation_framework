package org.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ExtentTestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(ExtentTestListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    private static String currentReportPath;

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite started: {}", context.getName());

        if (extent == null) {
            extent = new ExtentReports();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            currentReportPath = "target/test-reports/extent-report-" + timestamp + ".html";

            // Create directories if they don't exist
            try {
                Files.createDirectories(Paths.get("target/test-reports"));
            } catch (IOException e) {
                logger.error("Failed to create test reports directory", e);
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(currentReportPath);
            try {
                spark.loadXMLConfig("src/test/resources/extent-config.xml");
            } catch (IOException e) {
                logger.warn("Could not load extent config, using defaults", e);
            }

            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getMethod().getMethodName());
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        test.set(extentTest);

        // Log parameters if this is a data-driven test
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            StringBuilder paramLog = new StringBuilder("<b>Test Data:</b> <br>");
            for (int i = 0; i < parameters.length; i++) {
                paramLog.append("Param ").append(i + 1).append(": ").append(parameters[i].toString()).append("<br>");
            }
            extentTest.info(paramLog.toString());
        }

        // Try to get the page from the test class if it's a BaseTest or BaseLoginTest
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof org.example.base.BaseTest) {
                org.example.base.BaseTest baseTest = (org.example.base.BaseTest) testInstance;
                Page page = baseTest.getPage();
                pageThreadLocal.set(page);
            } else if (testInstance instanceof org.example.base.BaseLoginTest) {
                org.example.base.BaseLoginTest baseLoginTest = (org.example.base.BaseLoginTest) testInstance;
                Page page = baseLoginTest.getPage();
                pageThreadLocal.set(page);
            }
        } catch (Exception e) {
            logger.warn("Could not access page for screenshot capture", e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        test.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());

        ExtentTest extentTest = test.get();
        extentTest.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());

        // Capture screenshot on failure
        captureScreenshot("FAILURE_" + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        test.get().log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite finished: {}", context.getName());
        if (extent != null) {
            extent.flush();
        }
    }

    private void captureScreenshot(String screenshotName) {
        Page page = pageThreadLocal.get();
        if (page != null) {
            try {
                String screenshotPath = "target/test-reports/screenshots/" + screenshotName + "_" +
                        System.currentTimeMillis() + ".png";

                // Create screenshots directory
                Files.createDirectories(Paths.get("target/test-reports/screenshots"));

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));

                // Encode file to Base64 so it can be embedded directly in the HTML report
                byte[] fileContent = Files.readAllBytes(Paths.get(screenshotPath));
                String base64Image = Base64.getEncoder().encodeToString(fileContent);

                // Attach screenshot to report as Base64 Image
                test.get().fail("Screenshot captured",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());

                logger.info("Screenshot captured: {}", screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to capture screenshot", e);
            }
        }
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void logInfo(String message) {
        logger.info(message);
        if (test.get() != null) {
            test.get().log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        logger.info("PASS: {}", message);
        if (test.get() != null) {
            test.get().log(Status.PASS, message);
        }
    }

    public static void logFail(String message) {
        logger.error("FAIL: {}", message);
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
        }
    }

    public static String getReportPath() {
        return currentReportPath;
    }
}