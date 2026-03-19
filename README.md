# UI Automation Framework (Deployment Manager Testing)

A robust, Java-based UI automation testing framework built to test the Deployment Manager application. The framework utilizes a centralized TOML configuration and features automatic test reporting.

## Key Features

- **Centralized Configuration:** All settings (URLs, browser behavior, test suites, timeouts, logging) are managed in a single `config.toml` file.
- **Email Reporting:** Automatic HTML-formatted email reports with test execution results triggered at the end of runs via an integrated email reporter.
- **Test Suites Manager:** Define and execute customized test suites directly from the configuration without modifying source code.
- **CI/CD Ready:** Supports overriding configuration variables via command-line arguments and seamlessly accepts environment variables for sensitive data.

## Project Structure

```text
├── app/src/test/java/org/example/
│   ├── base/           # Base classes for test initialization
│   ├── listeners/      # Test listeners for execution tracking and reporting
│   ├── pages/          # Page Object abstractions (POM)
│   ├── tests/          # Test execution scripts
│   └── utils/          # Helpers (TOML parsing, EmailReporter, SuiteRunner)
├── app/src/test/resources/
│   └── config.toml     # Primary TOML configuration properties
├── CONFIG_SUMMARY.md   # Setup summary documentation
├── QUICK_REFERENCE.md  # Short reference for framework usage
└── TOML_CONFIG_GUIDE.md# Detailed guide on configuring the framework
```

## Quick Start

### 1. Configure Testing Environment
Ensure your settings are correctly mapped inside the primary config file (`app/src/test/resources/config.toml`):
```toml
[deployment_manager]
url = "https://your-dm-url/am/login"
```

If you are using the email reporting feature (`email.enabled = true`), securely set the required environment variables:
```bash
export EMAIL_PASSWORD="your_app_password"
```

### 2. Execute Tests
Run the framework using the included Gradle wrapper from the root of the project:

```bash
# Run all tests
./gradlew clean test

# Run a specific test class
./gradlew clean test --tests "org.example.tests.MapPageTest.*"

# Run tests overriding the configuration to headless mode
./gradlew clean test -Dbrowser.headless=true
```

## Further Documentation

For an in-depth understanding of the framework's configuration and capabilities, refer to the following guides included in the repository:
- `QUICK_REFERENCE.md`: Quick overview of the framework features and structure.
- `TOML_CONFIG_GUIDE.md`: Deep dive into editing and extending the framework configuration.
- `TEST_LOGGING_README.md`: Information regarding the customized file loggers.
- `CONFIG_SUMMARY.md` & `IMPLEMENTATION_CHECKLIST.md`: Insights into recent architecture updates.
