package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.utils.testUtilities;
import org.example.utils.testUtilities.LoginHelpers;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @AfterMethod
    public void afterEachTest() throws InterruptedException {
        Thread.sleep(10000);
    }
    
    //Test valid fleet names
    @Test(priority = 1, dataProvider = "validFleetNames", dataProviderClass = testUtilities.class)
    public void testValidFleetNames(String fleetName) {
        HomePage home = new HomePage(page);
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        home.createNewFleet(fleetName);
        logger.info("Fleet created");
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation success alert not visible!");
        Assert.assertTrue(home.searchFleet(fleetName), "Created fleet is not visible");
    }

    //Test invalid fleet names
    @Test(priority = 2, dataProvider = "invalidFleetNames", dataProviderClass = testUtilities.class)
    public void testInvalidFleetNames(String fleetName) {
        HomePage home = new HomePage(page);
        home.createNewFleet(fleetName);
        logger.info("Fleet created");
        boolean invalidLabelVisible = home.isInvalidFleetName();
        boolean emptyNameLabelVisible = home.isFleetNameEmpty();
        Assert.assertTrue(invalidLabelVisible || emptyNameLabelVisible,
                "Expected either invalid-fleet-name label or empty-fleet-name label to be visible");
    }

    //Test new invalid fleet names
    @Test(priority = 3, dataProvider = "newInvalidFleetNames", dataProviderClass = testUtilities.class)
    public void testNewInvalidFleetNames(String oldFleetName, String newFleetName) {
        HomePage home = new HomePage(page);
        home.editExistingFleetName(oldFleetName, newFleetName);
        logger.info("Fleet edited");
        boolean invalidLabelVisible = home.isInvalidFleetName();
        boolean emptyNameLabelVisible = home.isFleetNameEmpty();
        Assert.assertTrue(invalidLabelVisible || emptyNameLabelVisible,
                "Expected either invalid-fleetName label or emptyFleetName label to be visible");
    }

    //Test fleet deletion
    @Test(priority = 4, dataProvider = "validFleetNames", dataProviderClass = testUtilities.class)
    public void testFleetDeletion(String fleetName) {
        HomePage home = new HomePage(page);
        home.deleteExistingFleet(fleetName);
        logger.info("Fleet deleted");
        Assert.assertTrue(home.isFleetDeletionSuccess(), "Fleet wasn't deleted");
    }

    //Test manage fleet button navigates to correct management page
    @Test(priority = 5)
    public void testManageFleetButtonNavigatesToCorrectManagementPage() {
        HomePage home = new HomePage(page);
        String fleetName = "managefleetButtonTestDummy";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        home.createNewFleet(fleetName);
        logger.info("Fleet created");
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation should succeed for: " + fleetName);
        Assert.assertTrue(home.searchFleet(fleetName), "Created fleet should be visible in listing for: " + fleetName);
        home.clickManageFleetFor(fleetName);
        Assert.assertTrue(home.isOnFleetManagementPage(fleetName), "Fleet management UI should show heading '"
                + fleetName + "', Maps/Sherpas tabs, and 'Assign a Map' prompt.");
    }

    //Test valid fleet search
    @Test(priority = 6)
    public void testValidFleetSearch() {
        HomePage home = new HomePage(page);
        String fleetName = "searchFleetTestDummy";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        home.createNewFleet(fleetName);
        logger.info("Fleet created");
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation should succeed for: " + fleetName);
        Assert.assertTrue(home.searchFleet(fleetName), "Created fleet should be visible in listing for: " + fleetName);
    }

    //Test invalid fleet search
    @Test(priority = 7)
    public void testInvalidFleetSearch() {
        HomePage home = new HomePage(page);
        String fleetName = "invalidFleetSearchTestDummy";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        Assert.assertFalse(home.searchFleet(fleetName), "fleet should not be visible in listing for: " + fleetName);
    }

    //Test fleet search case sensitivity
    @Test(priority = 7)
    public void testFleetSearchCaseSensitivity() {
        HomePage home = new HomePage(page);
        String fleetName = "testFLEET";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        home.createNewFleet(fleetName);
        logger.info("Fleet created");
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation should succeed for: " + fleetName);
        Assert.assertTrue(home.searchFleet(fleetName), "Created fleet should be visible in listing for: " + fleetName);
        home.searchFleet(fleetName.toLowerCase());
        Assert.assertTrue(home.searchFleet(fleetName.toLowerCase()),
                "Created fleet should be visible in listing for: " + fleetName.toLowerCase());
        home.searchFleet(fleetName.toUpperCase());
        Assert.assertTrue(home.searchFleet(fleetName.toUpperCase()),
                "Created fleet should be visible in listing for: " + fleetName.toUpperCase());
    }

    //Test empty fleet search
    @Test(priority = 8)
    public void testEmptyFleetSearch() {
        HomePage home = new HomePage(page);
        String fleetName1 = "testFleet1";
        String fleetName2 = "testFleet2";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName1);
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName2);
        logger.info("Fleets cleaned up");
        home.createNewFleet(fleetName1);
        home.createNewFleet(fleetName2);
        logger.info("Fleets created");
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation should succeed for: " + fleetName1);
        Assert.assertTrue(home.isFleetCreationSuccess(), "Fleet creation should succeed for: " + fleetName2);
        Assert.assertTrue(home.searchFleet(fleetName1),
                "Created fleet should be visible in listing for: " + fleetName1);
        Assert.assertTrue(home.searchFleet(fleetName2),
                "Created fleet should be visible in listing for: " + fleetName2);
        home.clearFleetFilter();
        Assert.assertTrue(home.isFleetVisible(fleetName1), "Fleet 1 should be visible after empty search");
        Assert.assertTrue(home.isFleetVisible(fleetName2), "Fleet 2 should be visible after empty search");
    }

    //Test no result found text
    @Test(priority = 9)
    public void testNoResultFoundText() {
        HomePage home = new HomePage(page);
        String fleetName = "randomNameForFleet";
        LoginHelpers.cleanExistingFleetIfAny(page, fleetName);
        logger.info("Fleet cleaned up");
        Assert.assertFalse(home.searchFleet(fleetName), "fleet should not be visible in listing for: " + fleetName);
        Assert.assertTrue(home.isNoResultFoundVisible(), "No result found text should be visible");
    }
}
