package org.example.base;

import java.io.IOException;
import java.nio.file.Paths;
import org.example.pages.HomePage;
import org.example.pages.MapPage;
import org.example.pages.MapEditorV2;
import org.example.utils.TomlConfigManager;
import org.example.utils.testUtilities;
import org.example.utils.testUtilities.LoginHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class MapEditorBaseTestV2 {

    protected static final Logger logger = LoggerFactory.getLogger(MapEditorBaseTestV2.class);
    protected TomlConfigManager configManager;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected String currentMapName; // Track the map we imported

    @BeforeClass
    public void setupMapEditor() throws IOException {
        logger.info("Setting up MapEditor test environment (Class Level)");
        this.configManager = TomlConfigManager.getInstance();
        this.playwright = Playwright.create();
        // Get headless mode and browser type from config
        boolean headless = configManager.isHeadless();
        String browserType = configManager.getBrowserType();
        int slowMo = configManager.getSlowMo();
        int viewportWidth = configManager.getViewportWidth();
        int viewportHeight = configManager.getViewportHeight();
        logger.info("Browser configuration - Type: {}, Headless: {}, SlowMo: {}ms, Viewport: {}x{}", browserType,
                headless, slowMo, viewportWidth, viewportHeight);
        this.browser = this.playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(slowMo));
        this.context = this.browser.newContext(new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true)
                .setViewportSize(viewportWidth, viewportHeight));
        this.page = this.context.newPage();
        // Navigate to DM URL from config
        String dmUrl = configManager.getDMUrl();
        this.page.navigate(dmUrl);
        logger.info("Browser setup complete, navigated to: {}", dmUrl);
        // 1. Login
        logger.info("Logging in...");
        LoginHelpers.loginWithFirstValidUser(page);
        // 2. Navigate to Map Page
        logger.info("Navigating to Map Page...");
        HomePage home = new HomePage(page);
        home.clickMapMenu();
        MapPage mapPage = new MapPage(page);
        if (!mapPage.isMapHeaderVisible()) {
            throw new RuntimeException("Failed to navigate to Map Page");
        }
        // 3. Import Empty Map
        logger.info("Importing Empty Map...");
        String[] mapData = testUtilities.MapDataHelpers.getEmptyMapData();
        String mapFileName = mapData[0];
        String mapName = mapData[1];
        this.currentMapName = mapName;

        // Path to map file
        String mapFilePath = Paths.get("src", "test", "resources", "config", "map_Files", mapFileName).toAbsolutePath()
                .toString();

        // Clean up if exists
        System.out.println("Deleting map if exists: " + mapName);
        mapPage.deleteMapIfExists(mapName);

        // Import
        System.out.println("Importing map: " + mapName);
        mapPage.importV2Map(mapFilePath);
        if (!mapPage.isMapUploadedSuccessfully(mapName)) {
            throw new RuntimeException("Failed to import map: " + mapName);
        }
        logger.info("Map imported suqccessfully: {}", mapName);

        // 4. Enter Edit Mode
        logger.info("Entering Edit Mode for map: {}", mapName);
        mapPage.clickEditMap(mapName);

        MapEditorV2 editor = new MapEditorV2(page);
        editor.selectV2mapStyle();
        logger.info("Map style selected");
        System.out.println("Map style selected");

        // Verify we are in editor
        if (editor.isInMapEditor()) {
            System.out.println("Successfully entered Edit mode");
        } else {
            System.out.println("Failed to enter Edit mode");
            throw new RuntimeException("Failed to enter Edit Mode");
        }

    }

    @AfterClass
    public void tearDownMapEditor() {
        logger.info("Tearing down MapEditor test environment");

        // if (page != null) {
        // // Try to exit editor and delete map
        // try {
        // // Determine how to exit editor. Usually clicking 'Maps' breadcrumb or menu?
        // // For now, assuming we can just navigate back to maps or simply delete.
        // // But we are inside the editor. We need to go back to Map List to delete.
        // // Re-clicking Map Menu should work.
        // HomePage home = new HomePage(page);
        // home.clickMapMenu();

        // MapPage mapPage = new MapPage(page);
        // if (currentMapName != null) {
        // logger.info("Deleting map: {}", currentMapName);
        // mapPage.deleteMapIfExists(currentMapName);
        // }
        // } catch (Exception e) {
        // logger.error("Error during map cleanup: ", e);
        // }

        // page.close();
        // }
        if (context != null)
            context.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
        logger.info("Browser cleanup complete");
    }

    // Getter for page
    public Page getPage() {
        return page;
    }
}
