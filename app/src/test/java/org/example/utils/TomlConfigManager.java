package org.example.utils;

import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TomlConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(TomlConfigManager.class);
    private static TomlConfigManager instance;
    private Toml toml;

    private TomlConfigManager() {
        loadConfig();
    }

    public static synchronized TomlConfigManager getInstance() {
        if (instance == null) {
            instance = new TomlConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            // Try to load from classpath resources first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.toml");

            if (inputStream != null) {
                this.toml = new Toml().read(inputStream);
                logger.info("Loaded config.toml from classpath resources");
            } else {
                // Try to load from file system
                Path configPath = Paths.get("app/src/test/resources/config.toml");
                if (Files.exists(configPath)) {
                    this.toml = new Toml().read(configPath.toFile());
                    logger.info("Loaded config.toml from: {}", configPath.toAbsolutePath());
                } else {
                    throw new RuntimeException("config.toml file not found in resources or filesystem");
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Failed to load config.toml", e);
        }
    }

    // =====================
    // Deployment Manager
    // =====================
    public String getDMUrl() {
        return getStringValue("deployment_manager.url", "https://qa_dm.atimotors.com/am/login");
    }

    // =====================
    // Browser Settings
    // =====================
    public boolean isHeadless() {
        return getBooleanValue("browser.headless", false);
    }

    public String getBrowserType() {
        return getStringValue("browser.type", "chromium");
    }

    public int getSlowMo() {
        return getIntValue("browser.slow_mo", 0);
    }

    public int getViewportWidth() {
        return getIntValue("browser.viewport_width", 1920);
    }

    public int getViewportHeight() {
        return getIntValue("browser.viewport_height", 1080);
    }

    // =====================
    // Timeouts
    // =====================
    public long getPageLoadTimeout() {
        return getLongValue("timeouts.page_load", 30000L);
    }

    public long getElementWaitTimeout() {
        return getLongValue("timeouts.element_wait", 5000L);
    }

    public long getDefaultTimeout() {
        return getLongValue("timeouts.default", 10000L);
    }

    // =====================
    // Logging
    // =====================
    public String getLogLevel() {
        return getStringValue("logging.level", "INFO");
    }

    public boolean isConsoleLoggingEnabled() {
        return getBooleanValue("logging.console", true);
    }

    // =====================
    // Email Settings
    // =====================
    public boolean isEmailEnabled() {
        System.out.println("Email enabled: " + getBooleanValue("email.enabled", false));
        return getBooleanValue("email.enabled", false);
    }

    public String getSmtpHost() {
        System.out.println("SMTP host: " + getStringValue("email.smtp.host", "smtp.gmail.com"));
        return getStringValue("email.smtp.host", "smtp.gmail.com");
    }

    public int getSmtpPort() {
        System.out.println("SMTP port: " + getIntValue("email.smtp.port", 587));
        return getIntValue("email.smtp.port", 587);
    }

    public boolean isSmtpAuthEnabled() {
        System.out.println("SMTP auth enabled: " + getBooleanValue("email.smtp.auth", true));
        return getBooleanValue("email.smtp.auth", true);
    }

    public boolean isSmtpStartTlsEnabled() {
        System.out.println("SMTP starttls enabled: " + getBooleanValue("email.smtp.starttls", true));
        return getBooleanValue("email.smtp.starttls", true);
    }

    public String getSenderEmail() {
        System.out.println("Sender email: " + getStringValue("email.sender.address", ""));
        String email = getStringValue("email.sender.address", "");
        return resolveEnvVariables(email);
    }

    public String getSenderPassword() {
        System.out.println("Sender password: " + getStringValue("email.sender.password", ""));
        String password = getStringValue("email.sender.password", "");
        return resolveEnvVariables(password);
    }

    public String[] getRecipients() {
        System.out.println("Recipients: " + getListValue("email.recipients.to"));
        List<String> recipients = getListValue("email.recipients.to");
        if (recipients == null || recipients.isEmpty()) {
            return new String[0];
        }
        return recipients.toArray(new String[0]);
    }

    public String getEmailSubject() {
        System.out.println("Email subject: " + getStringValue("email.report.subject", "Automation Test Report"));
        return getStringValue("email.report.subject", "Automation Test Report");
    }

    public boolean shouldAttachReport() {
        System.out.println("Attach report: " + getBooleanValue("email.report.attach_report", true));
        return getBooleanValue("email.report.attach_report", true);
    }

    // =====================
    // Test Suites
    // =====================
    public String[] getSuite(String suiteName) {
        List<String> tests = getListValue("test_suites." + suiteName + ".tests");
        if (tests == null || tests.isEmpty()) {
            logger.warn("Test suite '{}' not found or is empty", suiteName);
            return new String[0];
        }
        return tests.toArray(new String[0]);
    }

    public Map<String, Object> getAllSuites() {
        Toml testSuitesToml = toml.getTable("test_suites");
        if (testSuitesToml == null) {
            return Map.of();
        }
        return testSuitesToml.toMap();
    }

    public String[] getAvailableSuites() {
        Map<String, Object> suites = getAllSuites();
        return suites.keySet().toArray(new String[0]);
    }

    // =====================
    // Database Settings (for future use)
    // =====================
    public String getDatabaseHost() {
        return getStringValue("database.host", "localhost");
    }

    public int getDatabasePort() {
        return getIntValue("database.port", 5432);
    }

    public String getDatabaseName() {
        return getStringValue("database.name", "test_db");
    }

    public String getDatabaseUsername() {
        return getStringValue("database.username", "testuser");
    }

    public String getDatabasePassword() {
        String password = getStringValue("database.password", "");
        return resolveEnvVariables(password);
    }

    // =====================
    // Reports
    // =====================
    public String getReportDirectory() {
        return getStringValue("reports.dir", "target/test-reports");
    }

    public String getReportFormat() {
        return getStringValue("reports.format", "html");
    }

    public boolean isDetailedReportingEnabled() {
        return getBooleanValue("reports.detailed", true);
    }

    // =====================
    // Helper Methods
    // =====================

    /**
     * Get string value from TOML with dot notation support
     */
    private String getStringValue(String key, String defaultValue) {
        try {
            String[] parts = key.split("\\.");
            Object current = toml.toMap();

            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(part);
                } else {
                    return defaultValue;
                }
            }

            if (current == null) {
                return defaultValue;
            }

            String value = current.toString();
            return value.isEmpty() ? defaultValue : value;
        } catch (Exception e) {
            logger.debug("Error retrieving value for key: {}", key, e);
            return defaultValue;
        }
    }

    /**
     * Get boolean value from TOML with dot notation support
     */
    private boolean getBooleanValue(String key, boolean defaultValue) {
        try {
            String[] parts = key.split("\\.");
            Object current = toml.toMap();

            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(part);
                } else {
                    return defaultValue;
                }
            }

            if (current == null) {
                return defaultValue;
            }

            if (current instanceof Boolean) {
                return (Boolean) current;
            }

            return Boolean.parseBoolean(current.toString());
        } catch (Exception e) {
            logger.debug("Error retrieving boolean value for key: {}", key, e);
            return defaultValue;
        }
    }

    /**
     * Get integer value from TOML with dot notation support
     */
    private int getIntValue(String key, int defaultValue) {
        try {
            String[] parts = key.split("\\.");
            Object current = toml.toMap();

            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(part);
                } else {
                    return defaultValue;
                }
            }

            if (current == null) {
                return defaultValue;
            }

            if (current instanceof Long) {
                return ((Long) current).intValue();
            }

            if (current instanceof Integer) {
                return (Integer) current;
            }

            return Integer.parseInt(current.toString());
        } catch (Exception e) {
            logger.debug("Error retrieving integer value for key: {}", key, e);
            return defaultValue;
        }
    }

    /**
     * Get long value from TOML with dot notation support
     */
    private long getLongValue(String key, long defaultValue) {
        try {
            String[] parts = key.split("\\.");
            Object current = toml.toMap();

            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(part);
                } else {
                    return defaultValue;
                }
            }

            if (current == null) {
                return defaultValue;
            }

            if (current instanceof Long) {
                return (Long) current;
            }

            if (current instanceof Integer) {
                return ((Integer) current).longValue();
            }

            return Long.parseLong(current.toString());
        } catch (Exception e) {
            logger.debug("Error retrieving long value for key: {}", key, e);
            return defaultValue;
        }
    }

    /**
     * Get list value from TOML with dot notation support
     */
    private List<String> getListValue(String key) {
        try {
            String[] parts = key.split("\\.");
            Object current = toml.toMap();

            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(part);
                } else {
                    return null;
                }
            }

            if (current instanceof List) {
                return (List<String>) current;
            }

            return null;
        } catch (Exception e) {
            logger.debug("Error retrieving list value for key: {}", key, e);
            return null;
        }
    }

    /**
     * Resolve environment variables in format ${VAR_NAME}
     */
    private String resolveEnvVariables(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        String pattern = "\\$\\{([^}]+)\\}";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(value);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String envVar = m.group(1);
            String envValue = System.getenv(envVar);
            if (envValue == null) {
                envValue = System.getProperty(envVar, "");
            }
            m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(envValue));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    /**
     * Reload configuration (useful for testing)
     */
    public void reload() {
        logger.info("Reloading configuration...");
        loadConfig();
    }

    /**
     * Print all configuration to logs (use with caution - may contain sensitive
     * data)
     */
    public void printConfig() {
        logger.info("Current Configuration:");
        logger.info("  DM URL: {}", getDMUrl());
        logger.info("  Browser Headless: {}", isHeadless());
        logger.info("  Email Enabled: {}", isEmailEnabled());
        logger.info("  Available Suites: {}", String.join(", ", getAvailableSuites()));
    }
}
