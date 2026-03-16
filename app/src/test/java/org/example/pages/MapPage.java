package org.example.pages;

import java.nio.file.Paths;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class MapPage {
    private final Page page;

    private final Locator mapFleetSearchbar;
    private final Locator importButton;
    private final Locator uploadMapDialogBox;
    private final Locator mapPageHeader;
    private final Locator uploadMapBtn;
    private final Locator finalUploadBtn;
    private final Locator deleteButton;
    private final Locator confirmDeleteButton;
    private final Locator editButton;
    private final Locator UploadingDialog;


    public MapPage(Page page) {
        this.page = page;

        // Initialize locators (no actions in constructor)
        mapFleetSearchbar = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search Maps"));
        importButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Import"));
        uploadMapDialogBox = page.getByText("Upload Map", new Page.GetByTextOptions().setExact(true));
        mapPageHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Maps"));
        uploadMapBtn = page.getByText("Upload Map file");
        finalUploadBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload"));
        deleteButton = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Delete"));
        confirmDeleteButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete"));
        editButton = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Edit"));
        UploadingDialog = page.getByText("Uploading Map Files....");
    }

    // Check if Maps page header is visible
    public boolean isMapHeaderVisible() {
        return mapPageHeader.isVisible();
    }

    // Check if upload dialog is visible
    public boolean isUploadDialogVisible() {
        try {
            uploadMapDialogBox.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return uploadMapDialogBox.isVisible();
        } catch (PlaywrightException e) {
            System.out.println("Upload dialog box did not appear");
            return false;
        }
    }

    // Search for a map by name
    public void searchMap(String mapName) {
        mapFleetSearchbar.click();
        System.out.println("Searching for map: " + mapName);
        mapFleetSearchbar.clear();
        System.out.println("Cleared search bar");
        mapFleetSearchbar.fill(mapName);
        System.out.println("Filled search bar with: " + mapName);
        page.waitForTimeout(1500); // Wait for search results to filter
    }

    // Check if map with given name is present in results
    public boolean isMapPresent(String mapName) {
        try {
            Locator mapNameLocator = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(mapName));
            mapNameLocator
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
            System.out.println("Map name locator: " + mapNameLocator);
            return mapNameLocator.first().isVisible();
        } catch (PlaywrightException e) {
            System.out.println("Error checking map visibility: " + e.getMessage());
            return false;
        }
    }

    public Locator deletedDialogBoxHeader(String mapName) {
        String mapDeletedDialogBoxHeaderText = "Map - " + mapName + " Deleted";
        return page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(mapDeletedDialogBoxHeaderText));
    }

    public Locator deletedDialogBoxContent() {
        return page.getByText("Data deleted along with map:");
    }

    public Locator OkayFordeletedDialogBox() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay"));
    }

    // Helper to find dots menu button for a specific map
    private Locator getMapMenuButton(String mapName) {
        return page.locator("div")
                .filter(new Locator.FilterOptions()
                        .setHas(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(mapName))))
                .locator("button.MuiButtonBase-root.MuiIconButton-root.MuiIconButton-sizeSmall")
                .first();
    }

    // Delete map if it exists (for clean test state)
    public void deleteMapIfExists(String mapName) {
        searchMap(mapName);
        if (isMapPresent(mapName)) {
            // Click the map card/item
            getMapMenuButton(mapName).click();
            // Click delete button
            deleteButton.click();
            confirmDeleteButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            confirmDeleteButton.click();
            deletedDialogBoxHeader(mapName).waitFor(new Locator.WaitForOptions().setTimeout(5000));

            // Confirm deletion
            if (deletedDialogBoxHeader(mapName).isVisible() && deletedDialogBoxContent().isVisible()) {
                OkayFordeletedDialogBox().waitFor(new Locator.WaitForOptions().setTimeout(5000));
                OkayFordeletedDialogBox().click();
                System.out.println("Confirmed deletion of map: " + mapName);
            } else {
                System.out.println("Deletion confirmation dialog did not appear for map: " + mapName);
            }
            // Wait briefly to ensure deletion completes
            page.waitForTimeout(5000);
        }
    }

    // Click on Edit button in the map context menu
    public void clickEditMap(String mapName) {
        searchMap(mapName);
        if (isMapPresent(mapName)) {
            getMapMenuButton(mapName).click();
            editButton.waitFor();
            editButton.click();
            System.out.println("Clicked Edit for map: " + mapName);
        } else {
            throw new RuntimeException("Map " + mapName + " not found to edit.");
        }
    }

    // Import map from given file path
    public void importV2Map(String filePath) {
        importButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        importButton.click();

        uploadMapBtn.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        uploadMapBtn.click();

        // Upload file from given path
        page.setInputFiles("input[type='file']", Paths.get(filePath));

        finalUploadBtn.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        finalUploadBtn.click();
    }


    public boolean isUploadingDialogVisible(){
        try {
            UploadingDialog.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return (UploadingDialog.isVisible());
        } catch (PlaywrightException e) {
            System.out.println("Uploading dialog box did not appear");
            return false;
        }
    }

    public void waitForUploadDialogToDisappear() {
        UploadingDialog.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    // Verify upload success toast and return message
    public String getUploadSuccessToast() {
        try {
            Locator toast = page.getByRole(AriaRole.ALERT).last();
            toast.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            return toast.textContent();
        } catch (PlaywrightException e) {
            System.out.println("Upload success toast not found");
            return "";
        }
    }

    // Check if specific map upload success message appears
    public boolean isMapUploadedSuccessfully(String mapName) {
        waitForUploadDialogToDisappear();
        try {
            String successText = "Map " + mapName + " uploaded";
            Locator mapUploadedDialogBox = page.getByRole(AriaRole.ALERT).getByText(successText,
                    new Locator.GetByTextOptions().setExact(false));
            mapUploadedDialogBox.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            return mapUploadedDialogBox.isVisible();
        } catch (PlaywrightException e) {
            System.out.println("Map uploaded alert not found for: " + mapName);
            return false;
        }
    }

    

}
