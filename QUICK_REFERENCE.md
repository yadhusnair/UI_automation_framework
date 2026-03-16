# TOML Configuration Quick Reference

## What's New

You now have a complete TOML-based configuration system that centralizes ALL configuration in a single `config.toml` file:

- ✅ DM URL management
- ✅ Browser settings (headless/headed mode, slowMo)
- ✅ Timeout configurations
- ✅ Email reporting setup
- ✅ Test suite definitions
- ✅ Logging configuration

## Files Created

### Configuration Files
- **`app/src/test/resources/config.toml`** - Main configuration file (TOML format)

### Utility Classes
- **`org.example.utils.TomlConfigManager`** - Reads and manages all config values
- **`org.example.utils.EmailReporter`** - Handles sending email reports with config values
- **`org.example.utils.TestSuiteManager`** - Manages test suites defined in config
- **`org.example.utils.SuiteRunner`** - Runs specific test suites
- **`org.example.listeners.TestListener`** - Tracks test results and sends email reports

### Updated Files
- **`app/src/test/java/org/example/base/BaseTest.java`** - Now loads from TOML config
- **`app/build.gradle`** - Added toml4j and email dependencies

## Quick Start

### 1. Configure Your DM URL
Edit `app/src/test/resources/config.toml`:
```toml
[deployment_manager]
url = "https://your-dm-url/am/login"
```

### 2. Configure Email (Optional)
```toml
[email]
enabled = true

[email.sender]
address = "your_email@gmail.com"
password = "${EMAIL_PASSWORD}"  # Use environment variable

[email.recipients]
to = ["team@company.com"]
```

Set environment variable:
```bash
export EMAIL_PASSWORD=your_app_password
```

### 3. Define Test Suites
```toml
[test_suites.smoke]
tests = [
    "org.example.tests.MapPageTest.testV2MapImportWithVerification"
]

[test_suites.regression]
tests = [
    "org.example.tests.MapPageTest.testV2MapImportWithVerification",
    "org.example.tests.AnotherTest.testMethod"
]
```

### 4. Run Tests
```bash
# Run all tests
./gradlew clean test

# Run specific test
./gradlew clean test --tests "org.example.tests.MapPageTest.testV2MapImportWithVerification"

# Run in headless mode (override config)
./gradlew clean test -Dbrowser.headless=true
```

## Configuration Options

| Setting | Default | Description |
|---------|---------|-------------|
| `deployment_manager.url` | https://qa_dm.atimotors.com/am/login | DM application URL |
| `browser.headless` | false | Run browser in headless mode |
| `browser.type` | chromium | Browser type (chromium, firefox, webkit) |
| `browser.slow_mo` | 0 | Slow down each action (ms) |
| `timeouts.page_load` | 30000 | Page load timeout (ms) |
| `timeouts.element_wait` | 5000 | Element wait timeout (ms) |
| `email.enabled` | true | Enable email reporting |
| `email.smtp.host` | smtp.gmail.com | SMTP server |
| `email.smtp.port` | 587 | SMTP port |
| `logging.level` | INFO | Log level |

## Using Configuration in Code

```java
import org.example.utils.TomlConfigManager;
import org.example.utils.EmailReporter;
import org.example.utils.TestSuiteManager;

public class MyTest extends BaseTest {
    @Test
    public void testExample() {
        // Access configuration
        TomlConfigManager config = TomlConfigManager.getInstance();
        String dmUrl = config.getDMUrl();
        boolean headless = config.isHeadless();
        
        // Use test suites
        TestSuiteManager suiteManager = TestSuiteManager.getInstance();
        String[] smokeTests = suiteManager.getSuite("smoke");
        
        // Send email reports (automatic via TestListener)
        // Manual sending:
        EmailReporter reporter = new EmailReporter();
        reporter.sendReport("smoke", "/path/to/report.html", "5 passed, 0 failed");
    }
}
```

## File Structure

```
app/src/test/
├── java/org/example/
│   ├── base/
│   │   └── BaseTest.java (loads TOML config automatically)
│   ├── listeners/
│   │   └── TestListener.java (sends email reports)
│   ├── pages/
│   │   └── MapPage.java
│   ├── tests/
│   │   └── MapPageTest.java
│   └── utils/
│       ├── TomlConfigManager.java (loads config.toml)
│       ├── EmailReporter.java (sends emails)
│       ├── TestSuiteManager.java (manages test suites)
│       ├── SuiteRunner.java (runs suites)
│       └── testUtilities.java
└── resources/
    └── config.toml (main configuration file)
```

## Key Features

✅ **Single Configuration File** - All settings in one TOML file
✅ **Environment Variables** - Use `${VAR_NAME}` for sensitive data
✅ **Email Reporting** - Automatic test report emails
✅ **Test Suite Management** - Define and run test suites from config
✅ **Flexible Configuration** - Change settings without code changes
✅ **CI/CD Ready** - Override settings via environment variables

## Documentation

See `TOML_CONFIG_GUIDE.md` for detailed configuration documentation.

## Support

If configuration is not loading:
1. Check `config.toml` exists in `app/src/test/resources/`
2. Verify TOML syntax is valid
3. Check logs for error messages
4. Run `TomlConfigManager.getInstance().printConfig()` to debug

---

**Happy testing!** Your automation framework is now fully configured and ready to go.
