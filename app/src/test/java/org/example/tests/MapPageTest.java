package org.example.tests;

import java.io.File;
import java.io.IOException;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.pages.MapPage;
import org.example.utils.testUtilities;
import org.example.utils.testUtilities.LoginHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MapPageTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(MapPageTest.class);

    @BeforeMethod
    public void loginBeforeTest() throws IOException {
        logger.info("Logging in before test");
        // Call parent setup first (handled automatically by TestNG)
        // Then login
        LoginHelpers.loginWithFirstValidUser(page);
        logger.info("Login completed");
    }

    @BeforeMethod
    public void navigateToMapPage() throws IOException {
        logger.info("Navigating to map page");
        HomePage home = new HomePage(page);
        logger.debug("Created a new homepage object");
        home.clickMapMenu();
        logger.info("Clicked map menu, navigation complete");
    }

    //Test V2 map import with verification
    @Test(priority = 1, dataProvider = "v2mapImport", dataProviderClass = testUtilities.class)
    public void testV2MapImportWithVerification(String zipFilePath) {
        MapPage map = new MapPage(page);
        logger.info("Starting V2 map import test with file: {}", zipFilePath);
        String mapName = new File(zipFilePath).getName().replace(".zip", "");
        logger.info("Deleting map is already present {}", mapName);
        map.deleteMapIfExists(mapName);
        map.importV2Map(zipFilePath);
        logger.debug("Map name extracted: {}", mapName);
        boolean uploadSuccess = map.isMapUploadedSuccessfully(mapName);
        logger.info("Upload success check: {}", uploadSuccess);
        Assert.assertTrue(uploadSuccess, "Map Upload toast not visible for " + mapName);
        boolean mapPresent = map.isMapPresent(mapName);
        logger.info("Map presence check: {}", mapPresent);
        Assert.assertTrue(mapPresent, "Imported map not visible in the map list: " + mapName);
        logger.info("V2 map import test completed successfully");
    }
}
