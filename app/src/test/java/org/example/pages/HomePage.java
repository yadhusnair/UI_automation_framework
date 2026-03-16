package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;

public class HomePage {
    private final Page page;

    // Main navigation locators
    private final Locator usersAndPermissionsMenu;
    // private final Locator mapsMenu;
    // private final Locator sherpaMenu;
    private final Locator usersMenu;
    // private final Locator rolesMenu;
    // private final Locator burgerMenuClose;
    // private final Locator addFleetHeading;
    // private final Locator nameLabel;
    private final Locator burgerMenu;
    private final Locator fleetsMenu;
    private final Locator assetsMenu;
    private final Locator addFleetButton;
    private final Locator fleetNameTextbox;
    private final Locator fleetCreateButton;
    private final Locator fleetSearcher;
    private final Locator mapsTab;
    private final Locator sherpasTab;
    private final Locator assignMapHeading;
    private final Locator manageFleetButton;
    private final Locator noResultFoundText;

    public HomePage(Page page) {
        this.page = page;
        // Main navigation
        // burgerMenuClose = page.getByRole(AriaRole.BUTTON, new
        // Page.GetByRoleOptions().setName("open drawer"));
        usersAndPermissionsMenu = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Users & Permissions Users &"));
        // sherpaMenu = page.getByRole(AriaRole.BUTTON, new
        // Page.GetByRoleOptions().setName("Sherpa"));
        usersMenu = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Users").setExact(true));
        // rolesMenu = page.getByRole(AriaRole.BUTTON, new
        // Page.GetByRoleOptions().setName("Roles"));
        // addFleetHeading = page.getByRole(AriaRole.HEADING, new
        // Page.GetByRoleOptions().setName("Add Fleet"));
        // nameLabel = page.getByRole(AriaRole.DIALOG, new
        // Page.GetByRoleOptions().setName("Add Fleet")).locator("label");
        burgerMenu = page.getByTestId("open-drawer");
        fleetsMenu = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Fleets"));
        assetsMenu = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Assets Assets"));
        addFleetButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Fleet"));
        fleetNameTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name"));
        fleetCreateButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CREATE"));
        fleetSearcher = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search fleets"));
        mapsTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Maps"));
        sherpasTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Sherpas"));
        assignMapHeading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Assign a Map"));
        manageFleetButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("MANAGE FLEET"));
        noResultFoundText = page.getByText("No results found");
    }

    // -------------------------------
    // Navigation helpers
    // -------------------------------
    private void clickBurgerMenu() {
        System.out.println("clicking burger menu");
        burgerMenu.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        burgerMenu.click();
    }

    public void clickFleetMenu() {
        fleetsMenu.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        System.out.println("clicking fleets menu");
        fleetsMenu.click();
    }

    private void clickAssetsMenu() {
        System.out.println("clicking assets menu");
        assetsMenu.click();
    }

    private void clickUsersAndPermissions() {
        if (!usersAndPermissionsMenu.isVisible()) {
            if (burgerMenu.isVisible()) {
                burgerMenu.click();
            }
        }
        usersAndPermissionsMenu.click();
    }

    private void clickUsersMenu() {
        System.out.println("clicking users and permissions menu");
        clickUsersAndPermissions();
        usersMenu.click();
    }

    public Locator mapHeader() {
        return page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Maps"));
    }

    public boolean isMapHeaderVisible() {
        Locator header = mapHeader();
        try {
            header.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return header.isVisible();
        } catch (PlaywrightException e) {
            System.out.println("Map header not visible" + e.getMessage());
            return false;
        }
    }

    public Locator mapsMenu() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Maps"));
    }

    public void clickMapMenu() {
        clickBurgerMenu();
        clickAssetsMenu();
        System.out.println("clicking asset menu");
        mapsMenu().click();
        System.out.println("clicking maps menu..");
        if (!isMapHeaderVisible()) {
            throw new RuntimeException("failed to open Maps page : header not visible after clicking the map menu");
        }
    }

    // private void clickRolesMenu() {
    // clickUsersAndPermissions();
    // rolesMenu.click();
    // }

    // -------------------------------
    // Fleet creation
    // -------------------------------
    public void createNewFleet(String fleetName) {
        System.out.println("Creating new fleet");
        clickFleetMenu();
        System.out.println("Fleet menu clicked");
        addFleetButton.click();
        System.out.println("Add fleet button clicked");
        fleetNameTextbox.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        fleetNameTextbox.fill(fleetName);
        fleetCreateButton.click();
        // Wait for response from backend
        page.waitForTimeout(2000);
    }

    private Locator getFleetSuccessAlert() {
        return page.getByRole(AriaRole.ALERT).getByText("Creation of Fleet successful");
    }

    public boolean isFleetCreationSuccess() {
        try {
            getFleetSuccessAlert().waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return getFleetSuccessAlert().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    private Locator getInvalidFleetNameLabel() {
        return page.getByText("Fleet name can only contain");
    }

    private Locator getNameRequiredLabel() {
        return page.getByText("Name is required");
    }

    public boolean isInvalidFleetName() {
        try {
            return getInvalidFleetNameLabel().isVisible();
        } catch (Exception e) {
            return false;

        }
    }

    public boolean isFleetNameEmpty() {
        try {
            return getNameRequiredLabel().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------------------
    // Fleet search
    // -------------------------------
    public boolean searchFleet(String fleetName) {
        fleetSearcher.click();
        fleetSearcher.clear();
        fleetSearcher.fill(fleetName);
        // Wait for search results to filter
        page.waitForTimeout(1500);
        return isFleetVisible(fleetName);
    }

    public void clearFleetFilter() {
        fleetSearcher.click();
        fleetSearcher.clear();
        // Wait for search results to refresh/reset
        page.waitForTimeout(1500);
    }

    public boolean isFleetVisible(String fleetName) {
        Locator fleetHeading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(fleetName));
        try {
            fleetHeading.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return fleetHeading.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoResultFoundVisible() {
        try {
            System.out.println("No result found text is visible");
            return noResultFoundText.isVisible();
        } catch (Exception e) {
            System.out.println("No result found text is not visible");
            return false;
        }
    }

    // -------------------------------
    // Fleet deletion
    // -------------------------------
    public void deleteExistingFleet(String fleetName) {
        fleetSearcher.click();
        fleetSearcher.clear();
        fleetSearcher.fill(fleetName);
        // Wait for search results to filter
        page.waitForTimeout(1500);
        Locator fleetCardHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(fleetName));
        Locator noResultLabel = page.getByText("No results found");
        try {
            fleetCardHeader.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception e) {
            throw new RuntimeException("Fleet '" + fleetName + "' not found after search");
        }
        if (noResultLabel.isVisible()) {
            throw new RuntimeException("Fleet '" + fleetName + "' not found");
        }
        Locator fleetCard = fleetCardHeader.locator("..");
        Locator fleetCardMenu = fleetCard.locator("#menu");
        fleetCardMenu.click();
        Locator deleteButton = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Delete"));
        deleteButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        deleteButton.click();
        Locator confirmDeleteButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("delete"));
        confirmDeleteButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        confirmDeleteButton.click();
    }

    private Locator getFleetDeletionSuccessAlert() {
        return page.getByRole(AriaRole.ALERT).getByText("Fleet deleted successfully");
    }

    public boolean isFleetDeletionSuccess() {
        try {
            getFleetDeletionSuccessAlert().waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return getFleetDeletionSuccessAlert().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------------------
    // Fleet edit
    // -------------------------------
    public boolean editExistingFleetName(String oldFleetName, String newFleetName) {
        fleetSearcher.click();
        fleetSearcher.clear();
        fleetSearcher.fill(oldFleetName);
        // Wait for search results to filter
        page.waitForTimeout(1500);
        Locator fleetCardHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(oldFleetName));
        Locator noResultLabel = page.getByText("No results found");
        try {
            fleetCardHeader.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception e) {
            throw new RuntimeException("Fleet '" + oldFleetName + "' not found");
        }
        if (noResultLabel.count() > 0 && noResultLabel.isVisible()) {
            throw new RuntimeException("Fleet '" + oldFleetName + "' not found");
        }
        Locator fleetCard = fleetCardHeader.locator("..");
        Locator fleetCardMenu = fleetCard.locator("#menu");
        fleetCardMenu.click();
        Locator editButton = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Edit"));
        editButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        editButton.click();
        fleetNameTextbox.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        fleetNameTextbox.click();
        fleetNameTextbox.fill(newFleetName);
        Locator updateBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update"));
        updateBtn.click();
        page.waitForTimeout(500);
        // Return false if invalid fleet name or empty
        return !(isInvalidFleetName() || isFleetNameEmpty());
    }

    // -------------------------------
    // navigite to manage fleet page
    // -------------------------------
    public void clickManageFleetFor(String fleetName) {
        searchFleet(fleetName);
        page.waitForTimeout(1500);
        Locator fleetCard = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(fleetName));
        int count = fleetCard.count();
        if (count != 1) {
            throw new RuntimeException(
                    "Expected exactly one fleet card for fleet name '" + fleetName + "' but found " + count);
        }
        manageFleetButton.click();
    }

    public boolean isFleetHeadingVisible(String fleetName) {
        Locator heading = page.getByText(fleetName);
        try {
            heading.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return heading.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMapsTabVisible() {
        System.out.println("maps tab visible");
        return mapsTab.isVisible();
    }

    public boolean isSherpasTabVisible() {
        System.out.println("sherpas tab visible");
        return sherpasTab.isVisible();
    }

    public boolean isOnFleetManagementPage(String fleetName) {
        System.out.println("on fleet management page");
        return isFleetHeadingVisible(fleetName) && isMapsTabVisible() && isSherpasTabVisible();
    }

}
