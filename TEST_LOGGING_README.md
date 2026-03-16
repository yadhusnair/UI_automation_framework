# Test Logging and Reporting Setup

This project now includes comprehensive test logging and custom reporting using SLF4J with Logback for logging and ExtentReports for detailed HTML test reports.

## Features Added

### 1. Logging
- **SLF4J** with **Logback** for structured logging
- Console and file logging with different log levels
- Log files stored in `build/test-logs/`
- Configurable log levels per package

### 2. Custom Reporting
- **ExtentReports** for beautiful HTML test reports
- Automatic screenshot capture on test failures
- Detailed test execution summary
- Reports stored in `build/test-reports/`
- Dark theme with customizable configuration

## How to Use

### Logging in Tests
Use the logger in your test classes:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTest {
    private static final Logger logger = LoggerFactory.getLogger(MyTest.class);

    @Test
    public void myTest() {
        logger.info("Starting test execution");
        // Your test code
        logger.debug("Debug information");
        // Assertions
        logger.info("Test completed successfully");
    }
}
```

### ExtentReports Integration
The `ExtentTestListener` automatically:
- Creates HTML reports for each test run
- Captures screenshots on failures
- Logs test steps and results

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "org.example.tests.MapPageTest"

# Run with custom TestNG XML
./gradlew test --tests "testng.xml"
```

### Viewing Reports
- **HTML Reports**: Open `build/test-reports/ExtentReport_*.html` in your browser
- **Log Files**: Check `build/test-logs/test.log` for detailed logs
- **Screenshots**: Failed test screenshots are in `build/test-reports/screenshots/`

## Configuration

### Logback Configuration
Located in `src/test/resources/logback.xml`:
- Console logging for quick feedback
- File logging with rolling policy
- Configurable log levels

### ExtentReports Configuration
Located in `src/test/resources/extent-config.xml`:
- Dark theme
- Custom title and headers
- Date/time formatting

## TestNG Listener
The `ExtentTestListener` is automatically registered in `testng.xml` and provides:
- Test lifecycle logging
- Screenshot capture on failures
- Report generation

## Dependencies Added
- `org.slf4j:slf4j-api:2.0.9`
- `ch.qos.logback:logback-classic:1.4.14`
- `com.aventstack:extentreports:5.1.1`
- `com.aventstack:extentreports-testng-adapter:1.2.2`

## Best Practices
1. Use appropriate log levels (DEBUG, INFO, WARN, ERROR)
2. Log important test steps and assertions
3. ExtentReports will automatically capture failures
4. Review HTML reports after test runs for detailed analysis
5. Screenshots help debug UI test failures

The setup provides enterprise-grade logging and reporting for your automated tests!