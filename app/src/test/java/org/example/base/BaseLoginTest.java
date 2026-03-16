package org.example.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.example.utils.TomlConfigManager;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseLoginTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected TomlConfigManager configManager;

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setup() throws java.io.IOException {
        logger.info("Setting up test environment");
        this.configManager = TomlConfigManager.getInstance();

        this.playwright = Playwright.create();

        // Get headless mode and browser type from config
        boolean headless = configManager.isHeadless();
        String browserType = configManager.getBrowserType();
        int slowMo = configManager.getSlowMo();

        logger.info("Browser configuration - Type: {}, Headless: {}, SlowMo: {}ms", browserType, headless, slowMo);

        this.browser = this.playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setSlowMo(slowMo));
        this.context = this.browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        this.page = this.context.newPage();

        // Navigate to DM URL from config
        String dmUrl = configManager.getDMUrl();
        this.page.navigate(dmUrl);
        logger.info("Browser setup complete, navigated to: {}", dmUrl);

    }

    @AfterMethod
    public void teardown() {
        logger.info("Tearing down login test environment");
        if (page != null)
            page.close();
        if (context != null)
            context.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
        logger.info("Browser cleanup complete");
    }

    // Getter for page to allow access from listeners
    public Page getPage() {
        return page;
    }
}
