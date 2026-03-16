# TOML Configuration Guide

## Overview

This automation test framework uses a single `config.toml` file to manage all configuration including:

- Deployment Manager URL
- Browser settings (headless/headed, type, slowMo)
- Timeouts and logging
- Email reporting settings
- Test suite definitions
- Database connections (for future use)

## Configuration File Location

```
app/src/test/resources/config.toml
```

## Config Structure

### 1. Deployment Manager

```toml
[deployment_manager]
url = "https://qa_dm.atimotors.com/am/login"
```

### 2. Browser Settings

```toml
[browser]
headless = false          # true for headless, false for headed mode
type = "chromium"         # chromium, firefox, or webkit
slow_mo = 0               # Milliseconds to slow down each action (0 = normal speed)
```

### 3. Timeouts

```toml
[timeouts]
page_load = 30000         # Page load timeout in ms
element_wait = 5000       # Element wait timeout in ms
default = 10000           # Default timeout in ms
```

### 4. Logging

```toml
[logging]
level = "INFO"            # TRACE, DEBUG, INFO, WARN, ERROR
console = true            # Enable console output
```

### 5. Email Reporting

#### Enable/Disable

```toml
[email]
enabled = true            # Set to false to disable email reporting
```

#### SMTP Configuration

```toml
[email.smtp]
host = "smtp.gmail.com"
port = 587
auth = true
starttls = true
```

#### Sender Credentials

```toml
[email.sender]
address = "yadhusnair20@gmail.com"
password = "${EMAIL_PASSWORD}"  # Use environment variable
```

#### Recipients

```toml
[email.recipients]
to = [
    "yadhusnair009@gmail.com"
]
```

#### Report Settings

```toml
[email.report]
subject = "Automation Test Report - {suite} - {timestamp}"
attach_report = true      # Attach HTML report to email
```

### 6. Test Suites

Define test suites with their test methods:

```toml
[test_suites.smoke]
tests = [
    "org.example.tests.MapPageTest.testV2MapImportWithVerification"
]

[test_suites.regression]
tests = [
    "org.example.tests.MapPageTest.testV2MapImportWithVerification",
    "org.example.tests.UserPageTest.testUserCreation"
]

[test_suites.full]
tests = [
    "org.example.tests.MapPageTest.testV2MapImportWithVerification",
    "org.example.tests.UserPageTest.testUserCreation",
    "org.example.tests.FleetPageTest.testFleetManagement"
]
```

### 7. Reports

```toml
[reports]
dir = "target/test-reports"       # Report directory
format = "html"                   # Report format
detailed = true                   # Enable detailed reporting
```

## Environment Variables

For sensitive information, use environment variables:

```bash
# Email password
export EMAIL_PASSWORD=your_app_password

# Database password
export DB_PASSWORD=your_db_password
```

Reference them in config.toml with `${VARIABLE_NAME}` syntax:

```toml
password = "${EMAIL_PASSWORD}"
```

## Using Configuration in Tests

### Access ConfigManager

```java
import org.example.utils.TomlConfigManager;

public class MyTest extends BaseTest {
    @Test
    public void testExample() {
        TomlConfigManager config = TomlConfigManager.getInstance();
        
        // Get values
        String dmUrl = config.getDMUrl();
        boolean headless = config.isHeadless();
        long timeout = config.getElementWaitTimeout();
        String[] recipients = config.getRecipients();
    }
}
```

### Access Test Suites

```java
import org.example.utils.TestSuiteManager;

public class SuiteExample {
    public static void main(String[] args) {
        TestSuiteManager suiteManager = TestSuiteManager.getInstance();
        
        // Get all suites
        String[] availableSuites = suiteManager.getAvailableSuites();
        
        // Get tests for a suite
        String[] smokeTests = suiteManager.getSuite("smoke");
        
        // Check if suite exists
        if (suiteManager.suiteExists("regression")) {
            // Run regression tests
        }
        
        // Add tests at runtime
        suiteManager.addTestToSuite("smoke", "org.example.tests.NewTest.testMethod");
    }
}
```

### Send Email Reports

```java
import org.example.utils.EmailReporter;

public class ReportExample {
    public static void main(String[] args) {
        EmailReporter reporter = new EmailReporter();
        
        // Send test report
        reporter.sendReport("smoke", "/path/to/report.html", "5 passed, 0 failed");
        
        // Send notification
        reporter.sendNotification("Tests Complete", "All tests finished successfully");
    }
}
```

## Running Tests with Different Configurations

### Run All Tests

```bash
./gradlew clean test
```

### Run Specific Suite

```bash
./gradlew clean test --tests "org.example.tests.MapPageTest.testV2MapImportWithVerification"
```

### Run in Headless Mode (Override Config)

```bash
./gradlew clean test -Dbrowser.headless=true
```

### Run with Custom DM URL (Override Config)

```bash
./gradlew clean test -Ddm.url=https://custom-dm.example.com/am/login
```

## Email Configuration Examples

### Gmail Setup

1. Enable 2-Factor Authentication on Google Account
2. Generate App Password at <https://myaccount.google.com/apppasswords>
3. Set environment variable:

   ```bash
   export EMAIL_PASSWORD=xxxx_xxxx_xxxx_xxxx
   ```

4. Update config.toml:

   ```toml
   [email.sender]
   address = "your_email@gmail.com"
   password = "${EMAIL_PASSWORD}"
   
   [email.recipients]
   to = ["team@company.com"]
   ```

### Office 365 / Outlook

```toml
[email.smtp]
host = "smtp.office365.com"
port = 587
auth = true
starttls = true
```

### Custom SMTP Server

```toml
[email.smtp]
host = "your-smtp-server.com"
port = 587
auth = true
starttls = true
```

## ConfigManager API Reference

### Deployment Manager

- `getDMUrl()` - Get DM URL

### Browser

- `isHeadless()` - Check if headless mode enabled
- `getBrowserType()` - Get browser type
- `getSlowMo()` - Get slow motion value in ms

### Timeouts

- `getPageLoadTimeout()` - Get page load timeout
- `getElementWaitTimeout()` - Get element wait timeout
- `getDefaultTimeout()` - Get default timeout

### Logging

- `getLogLevel()` - Get log level
- `isConsoleLoggingEnabled()` - Check if console logging enabled

### Email

- `isEmailEnabled()` - Check if email enabled
- `getSenderEmail()` - Get sender email address
- `getSenderPassword()` - Get sender password
- `getRecipients()` - Get recipient list (array)
- `getSmtpHost()` - Get SMTP host
- `getSmtpPort()` - Get SMTP port
- `getEmailSubject()` - Get email subject template

### Test Suites

- `getSuite(String suiteName)` - Get tests for a suite
- `getAvailableSuites()` - Get all suite names
- `getAllSuites()` - Get all suites as map

### Reports

- `getReportDirectory()` - Get report output directory
- `getReportFormat()` - Get report format
- `isDetailedReportingEnabled()` - Check if detailed reporting enabled

### Utilities

- `reload()` - Reload configuration from file
- `printConfig()` - Print current configuration (use with caution)

## Best Practices

1. **Sensitive Data**: Always use environment variables for passwords
2. **DM URL**: Keep the DM URL in config, not hardcoded in tests
3. **Headless Mode**: Use `headless = true` in CI/CD environments
4. **Email Recipients**: Keep the recipient list up to date
5. **Test Suites**: Keep smoke tests fast (< 5 minutes)
6. **Logging**: Use appropriate log levels for different environments
7. **Timeouts**: Adjust timeouts based on your network and server performance

## Troubleshooting

### Config Not Loading

- Ensure `config.toml` exists in `app/src/test/resources/`
- Check TOML syntax is valid
- Verify file permissions are readable

### Email Not Sending

- Check `email.enabled = true`
- Verify SMTP credentials and server
- Check recipient email addresses are valid
- Look for errors in logs

### Tests Not Finding Suites

- Verify suite name is correctly spelled in config.toml
- Check test method names are fully qualified
- Use `TestSuiteManager.getInstance().logAvailableSuites()` to debug

## Example CI/CD Integration

```bash
#!/bin/bash
# Run smoke tests in headless mode

export EMAIL_PASSWORD=${GMAIL_APP_PASSWORD}
export DM_URL=${TEST_ENVIRONMENT_URL}

./gradlew clean test \
    -Dbrowser.headless=true \
    --tests "org.example.tests.MapPageTest.testV2MapImportWithVerification"

# Check exit code
if [ $? -eq 0 ]; then
    echo "Tests passed!"
else
    echo "Tests failed!"
    exit 1
fi
```

## File Structure

```
app/
├── src/test/
│   ├── java/org/example/
│   │   ├── base/
│   │   │   └── BaseTest.java (loads config automatically)
│   │   ├── listeners/
│   │   │   └── TestListener.java (sends email reports)
│   │   ├── tests/
│   │   └── utils/
│   │       ├── TomlConfigManager.java (main config class)
│   │       ├── EmailReporter.java
│   │       ├── TestSuiteManager.java
│   │       └── SuiteRunner.java
│   └── resources/
│       └── config.toml (main configuration file)
```

## Next Steps

1. Update `config.toml` with your DM environment URL
2. Configure SMTP settings for email reporting
3. Add recipients to email config
4. Define your test suites
5. Run tests and verify email reports are sent

For more help, check the logs or open an issue in the repository.
