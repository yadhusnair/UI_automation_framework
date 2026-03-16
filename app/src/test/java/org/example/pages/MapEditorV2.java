package org.example.pages;

import com.microsoft.playwright.Locator;

import com.microsoft.playwright.Page;
import java.util.List;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.example.models.BaseZone;

public class MapEditorV2 {
    private static final Logger logger = LoggerFactory.getLogger(MapEditorV2.class);
    private final Page page;

    // stations section locators
    private final Locator StationsTab;
    private final Locator AddNewStationBtn;
    private final Locator StationNameInput;
    private final Locator XcordInput;
    private final Locator YcordInput;
    private final Locator stationAngleInput;
    private final Locator AutounhitchTag;
    private final Locator dispatchNotreqdTag;
    private final Locator dispatchOptionalTag;
    private final Locator parkingTag;
    private final Locator inplaceTag;
    private final Locator inplaceAntiClockwiseOption;
    private final Locator inplaceClockwiseOption;
    private final Locator selectAnticlockwiseOption;
    private final Locator selectClockwiseOption;
    private final Locator CustomTagsBar;
    private final Locator StationSearchBox;
    private final Locator InplaceDialog;
    private final Locator AutounhitchDialog;
    private final Locator DialogOkBtn;
    private final Locator trolleyHitchReleaseTag;
    private final Locator HideStationTag;
    private final Locator ChargingStationTag;

    // routes section locators
    private final Locator RoutesTab;
    private final Locator AddNewRouteBtn;
    private final Locator StartStationInput;
    private final Locator EndStationInput;
    private final Locator ReverseCheckBox;
    private final Locator SwitchDirectionBtn;
    private final Locator GenerateMirrorPathBtn;

    // gates section locators
    private final Locator GatesTab;
    private final Locator AddNewGateBtn;
    private final Locator BoxgateSelector;
    private final Locator LineGateSelector;
    private final Locator GateNameInput;
    private final Locator GateAngleInput;
    private final Locator GateReleaseDistanceInput;
    private final Locator GateApplyDistanceInput;

    // Zones section locators
    private final Locator AddNewZoneBtn;
    private final Locator ZoneNameInput;
    private final Locator ZoneAngleInput;
    private final Locator ZoneTypeSelector;
    private final Locator LOW_SPEED_ZONE_SELECTOR;
    private final Locator velocityFactorInput;
    private final Locator SPECIAL_CAMERA_ZONE_SELECTOR;
    private final Locator EntryHeadingInput;
    private final Locator RAMP_ZONE_SELECTOR;
    private final Locator UpRampHeadingInput;
    private final Locator GradientAngleInput;
    private final Locator VARIABLE_PADDING_ZONE_SELECTOR;
    private final Locator PaddingLeftInput;
    private final Locator PaddingRightInput;
    private final Locator PaddingFrontInput;
    private final Locator PaddingRearInput;
    private final Locator PayloadPaddingLeftInput;
    private final Locator PayloadPaddingRightInput;
    private final Locator OBSTACLE_AVOIDANCE_ZONE_SELECTOR;
    private final Locator MaximumOffsetInput;
    private final Locator TRAFFIC_INTERSECTION_ZONE_SELECTOR;
    private final Locator NO_GO_ZONES_SELECTOR;
    private final Locator TABLE_PICKUP_ZONES_SELECTOR;
    private final Locator VARIABLE_STOP_DIST_ZONES_SELECTOR;
    private final Locator StopDistanceInput;
    private final Locator SlowDistanceInput;
    private final Locator DOCKING_ZONES_SELECTOR;
    private final Locator EditZoneheader;

    // common locators
    private final Locator Canvas;
    private final Locator DeleteBtn;
    private final Locator DoneBtn;
    private final Locator ZonesTab;
    private final Locator RulerBtn;
    private final Locator GridBtn;
    private final Locator AttributetoggleBtn;
    private final Locator MapStyleSelectorDialogHeader;
    private final Locator V2option;
    private final Locator MapStyleSelectorDialogSubmitBtn;
    private final Locator SuccessMsgCloseBtn;
    private final Locator DeleteConfirmDialogYesBtn;
    private final Locator DeleteConfirmDialogNoBtn;

    public MapEditorV2(Page page) {
        this.page = page;

        // stations section locators
        this.StationsTab = page.getByLabel("Stations").locator("button");
        this.AddNewStationBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ADD NEW STATION"));
        this.StationNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Station Name *"));
        this.XcordInput = page.getByRole(AriaRole.SPINBUTTON, new Page.GetByRoleOptions().setName("X"));
        this.YcordInput = page.getByRole(AriaRole.SPINBUTTON, new Page.GetByRoleOptions().setName("Y"));

        this.stationAngleInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Station Angle"));
        this.AutounhitchTag = page.locator("input[name=\"auto_unhitch\"]");
        this.dispatchNotreqdTag = page.locator("input[name=\"dispatch_not_reqd\"]");
        this.dispatchOptionalTag = page.locator("input[name=\"dispatch_optional\"]");
        this.parkingTag = page.locator("input[name=\"parking\"]");
        this.inplaceTag = page.locator("input[name=\"inplace\"]");
        this.inplaceAntiClockwiseOption = page.getByRole(AriaRole.COMBOBOX,
                new Page.GetByRoleOptions().setName("Inplace Turn Anticlockwise"));
        this.inplaceClockwiseOption = page.getByRole(AriaRole.COMBOBOX,
                new Page.GetByRoleOptions().setName("Inplace Turn Clockwise"));
        this.selectAnticlockwiseOption = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("Anticlockwise").setExact(true));
        this.selectClockwiseOption = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("Clockwise").setExact(true));
        this.CustomTagsBar = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Custom Station tags"));
        this.InplaceDialog = page.getByText("'inplace' will always be true");
        this.AutounhitchDialog = page.getByText("'Dispatch Not Required' will");
        this.DialogOkBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok"));
        this.trolleyHitchReleaseTag = page.locator("input[name=\"trolley_hitch_release\"]");
        this.HideStationTag = page.locator("input[name=\"hidden\"]");
        this.ChargingStationTag = page.locator("input[name=\"charging\"]");

        // Routes section Locators
        this.AddNewRouteBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ADD NEW ROUTE"));
        this.StartStationInput = page.getByRole(AriaRole.COMBOBOX,
                new Page.GetByRoleOptions().setName("Start Station *"));
        this.EndStationInput = page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("End Station"));
        this.ReverseCheckBox = page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName("primary checkbox"));
        this.SwitchDirectionBtn = page.locator(".MuiBox-root.css-i0a0gj > .MuiButtonBase-root");
        this.GenerateMirrorPathBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Generate Mirror Path"));

        // gates section locators
        this.AddNewGateBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ADD NEW ZONE"));
        this.BoxgateSelector = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("EZ Box"));
        this.LineGateSelector = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("EZ Line gate"));
        this.GateNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("EZ Box Name *"));
        this.GateAngleInput = page.getByRole(AriaRole.SPINBUTTON, new Page.GetByRoleOptions().setName("Angle"));
        this.GateReleaseDistanceInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Transit Visa Release Distance"));
        this.GateApplyDistanceInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Apply Distance (m) *"));

        // Zones section locators
        this.AddNewZoneBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add New Zone"));
        this.ZoneNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Zone name *"));
        this.ZoneAngleInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Angle").setExact(true));
        this.ZoneTypeSelector = page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Zone Type"));
        this.LOW_SPEED_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("LOW_SPEED_ZONES"));
        this.velocityFactorInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Velocity Factor"));
        this.SPECIAL_CAMERA_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("SPECIAL_CAMERA_ZONES"));
        this.EntryHeadingInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Entry Heading"));
        this.RAMP_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("RAMP_ZONES"));
        this.UpRampHeadingInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Up Ramp Heading"));
        this.GradientAngleInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Gradient Angle"));
        this.VARIABLE_PADDING_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("VARIABLE_PADDING_ZONES"));
        this.PaddingLeftInput = page.locator("input[name=\"paddingLeft\"]");
        this.PaddingRightInput = page.locator("input[name=\"paddingRight\"]");
        this.PaddingFrontInput = page.locator("input[name=\"paddingFront\"]");
        this.PaddingRearInput = page.locator("input[name=\"paddingRear\"]");
        this.PayloadPaddingLeftInput = page.locator("input[name=\"payload_paddingLeft\"]");
        this.PayloadPaddingRightInput = page.locator("input[name=\"payload_paddingRight\"]");
        this.OBSTACLE_AVOIDANCE_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("OBSTACLE_AVOIDANCE_ZONES"));
        this.MaximumOffsetInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Maximum Offset"));
        this.TRAFFIC_INTERSECTION_ZONE_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("TRAFFIC_INTERSECTION_ZONES"));
        this.NO_GO_ZONES_SELECTOR = page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("NO_GO_ZONES"));
        this.TABLE_PICKUP_ZONES_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("TABLE_PICKUP_ZONES"));
        this.VARIABLE_STOP_DIST_ZONES_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("VARIABLE_STOP_DIST_ZONES"));
        this.StopDistanceInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Stop Distance"));
        this.SlowDistanceInput = page.getByRole(AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("Slow Distance"));
        this.DOCKING_ZONES_SELECTOR = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("DOCKING_ZONES"));
        this.EditZoneheader = page.getByText("Edit Zones");

        // common locator
        this.Canvas = page.locator("canvas").first();
        this.DeleteBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("DELETE"));
        this.DoneBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("DONE"));
        this.MapStyleSelectorDialogHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Select the Waypoint Version"));
        this.RoutesTab = page.getByTestId("RouteIcon").first();
        this.GatesTab = page.getByTestId("BorderHorizontalIcon").first();
        this.ZonesTab = page.getByTestId("AspectRatioIcon").first();
        this.RulerBtn = page.getByTestId("StraightenIcon").first();
        this.GridBtn = page.getByTestId("GridViewIcon").first();
        this.AttributetoggleBtn = page.locator("#toggle-button");
        this.V2option = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("V2"));
        this.MapStyleSelectorDialogSubmitBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit"));
        this.SuccessMsgCloseBtn = page.getByTestId("CloseIcon");
        this.DeleteConfirmDialogYesBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes"));
        this.DeleteConfirmDialogNoBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("No"));
        this.StationSearchBox = page.getByRole(AriaRole.TEXTBOX);

    }

    // -------------------
    // STATION ACTIONS
    // -------------------

    // select V2 map style
    public void selectV2mapStyle() {
        MapStyleSelectorDialogHeader
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        if (MapStyleSelectorDialogHeader.isVisible()) {
            V2option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            V2option.click();
            MapStyleSelectorDialogSubmitBtn.click();
        }
    }

    // add new station
    public void addNewStationV2(String stationName, double stationAngle, double xCord, double yCord, List<String> tags,
            List<String> customTags, boolean isClockwise) {
        clickAddNewStationBtn();
        System.out.println("Add New Station button clicked");
        fillStationName(stationName);
        System.out.println("Station name filled");
        fillStationXCordinate(xCord);
        System.out.println("X coordinate filled");
        fillStationYCordinate(yCord);
        System.out.println("Y coordinate filled");
        fillStationAngle(stationAngle);
        System.out.println("Station angle filled");
        selectTags(tags, isClockwise);
        System.out.println("Tags selected");
        selectCustomTags(customTags);
        System.out.println("Custom tags selected");
        clickDoneBtn();
        System.out.println("Station done button clicked");
    }

    // click add new Station button
    public void clickAddNewStationBtn() {
        System.out.println("Add New Station button clicked");
        AddNewStationBtn.click();
    }

    // click station tab
    public void clickStationTab() {
        System.out.println("Station tab clicked");
        StationsTab.click();
    }

    // search station
    public void searchStation(String StationName) {
        System.out.println("Searching for station: " + StationName);
        clickStationTab();
        StationSearchBox.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        StationSearchBox.fill(StationName);
        StationSearchBox.press("Enter");
    }

    // click station
    public void clickStation(String StationName) {
        searchStation(StationName);
        System.out.println("Clicking on station: " + StationName);
        Station(StationName).click();
    }

    // fill station name
    public void fillStationName(String StationName) {
        System.out.println("Filling station name: " + StationName);
        StationNameInput.clear();
        StationNameInput.click();
        StationNameInput.fill(StationName);
    }

    // fill station angle
    public void fillStationAngle(double stationAngle) {
        System.out.println("Filling station angle: " + stationAngle);
        stationAngleInput.clear();
        stationAngleInput.click();
        stationAngleInput.fill(String.valueOf(stationAngle));
    }

    // fill station x coordinate
    public void fillStationXCordinate(double xCord) {
        System.out.println("Filling station x coordinate: " + xCord);
        XcordInput.clear();
        XcordInput.click();
        XcordInput.fill(String.valueOf(xCord));
    }

    // fill station y coordinate
    public void fillStationYCordinate(double yCord) {
        System.out.println("Filling station y coordinate: " + yCord);
        YcordInput.clear();
        YcordInput.click();
        YcordInput.fill(String.valueOf(yCord));
    }

    // select tags
    public void selectTags(List<String> tags, boolean isClockwise) {
        System.out.println("Selecting tags: " + tags);
        if (tags != null) {
            for (String tag : tags) {
                switch (tag.toLowerCase()) {
                    case "autounhitch":
                        if (!AutounhitchTag.isChecked()) {
                            AutounhitchTag.click();
                            page.waitForTimeout(300); // small wait for UI animation if needed
                            if (AutounhitchDialog.isVisible()) {
                                DialogOkBtn.click();
                            }
                        }
                        break;
                    case "dispatch_not_reqd":
                    case "dispatchnotrequired":
                        if (!dispatchNotreqdTag.isChecked()) {
                            dispatchNotreqdTag.click();
                        }
                        break;
                    case "dispatch_optional":
                    case "dispatchoptional":
                        if (!dispatchOptionalTag.isChecked()) {
                            dispatchOptionalTag.click();
                        }
                        break;
                    case "parking":
                        if (!parkingTag.isChecked()) {
                            parkingTag.click();
                        }
                        break;
                    case "charging":
                        if (!ChargingStationTag.isChecked()) {
                            ChargingStationTag.click();
                        }
                        break;
                    case "hidden":
                        if (!HideStationTag.isChecked()) {
                            HideStationTag.click();
                        }
                        break;
                    case "trolley_hitch_release":
                        if (!trolleyHitchReleaseTag.isChecked()) {
                            trolleyHitchReleaseTag.click();
                            page.waitForTimeout(300);
                            if (InplaceDialog.isVisible()) {
                                DialogOkBtn.click();
                            }
                        }
                        break;
                    case "inplace":
                        if (!inplaceTag.isChecked()) {
                            inplaceTag.click();
                            page.waitForTimeout(300);
                            if (InplaceDialog.isVisible()) {
                                DialogOkBtn.click();
                            }
                        }
                        if (isClockwise) {
                            SwitchToClockwise(isClockwise);
                        } else {
                            SwitchToAntiClockwise(isClockwise);
                        }
                        break;
                }
            }
        }
    }

    // select custom tags
    public void selectCustomTags(List<String> customTags) {
        System.out.println("Selecting custom tags: " + customTags);
        if (customTags != null) {
            for (String tag : customTags) {
                CustomTagsBar.click();
                CustomTagsBar.fill(tag);
                CustomTagsBar.press("Enter");
            }
        }
    }

    // uncheck all tags
    public void unCheckAllTags() {
        if (AutounhitchTag.isChecked()) {
            AutounhitchTag.click();
        }
        if (dispatchNotreqdTag.isChecked()) {
            dispatchNotreqdTag.click();
        }
        if (dispatchOptionalTag.isChecked()) {
            dispatchOptionalTag.click();
        }
        if (parkingTag.isChecked()) {
            parkingTag.click();
        }
        if (ChargingStationTag.isChecked()) {
            ChargingStationTag.click();
        }
        if (HideStationTag.isChecked()) {
            HideStationTag.click();
        }
        if (trolleyHitchReleaseTag.isChecked()) {
            trolleyHitchReleaseTag.click();
        }
        if (inplaceTag.isChecked()) {
            inplaceTag.click();
        }
        System.out.println("Unchecked all tags");
    }

    // update station tags
    public void updateStationTags(List<String> tags, boolean isClockwise) {
        unCheckAllTags();
        selectTags(tags, isClockwise);
    }

    // update station custom tags
    public void updateStationCustomTags(List<String> currentCustomTags, List<String> newCustomTags) {
        DeleteCurrentCustomTag(currentCustomTags);
        selectCustomTags(newCustomTags);
    }

    // switch to clockwise
    public void SwitchToClockwise(boolean isClockwise) {
        if (isClockwise && inplaceAntiClockwiseOption.isVisible()) {
            inplaceAntiClockwiseOption.click();
            selectClockwiseOption.click();
        }
    }

    // switch to anti clockwise
    public void SwitchToAntiClockwise(boolean isClockwise) {
        if (!isClockwise && inplaceClockwiseOption.isVisible()) {
            inplaceClockwiseOption.click();
            selectAnticlockwiseOption.click();
        }
    }

    // click done button
    public void clickDoneBtn() {
        DoneBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        DoneBtn.click();
    }

    // click delete button
    public void clickDeleteBtn() {
        DeleteBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        DeleteBtn.click();
    }

    // close success message
    public void successMsgClose() {
        SuccessMsgCloseBtn
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        SuccessMsgCloseBtn.click();
    }

    // station delete confirm dialog
    public Locator stationDeleteConfirmDialog(String StationName) {
        return page.getByText("Are you sure you want to delete '" + StationName + "' station?NoYes");
    }

    // check station delete confirm dialog is visible
    public boolean isStationDeleteConfirmDialogVisible(String StationName) {
        return stationDeleteConfirmDialog(StationName).isVisible();
    }

    // delete station
    public void deleteStationV2(String stationName) {
        clickStation(stationName);
        clickDeleteBtn();
        if (isStationDeleteConfirmDialogVisible(stationName)) {
            DeleteConfirmDialogYesBtn.click();
        } else {
            logger.info("Station delete confirm dialog is not visible");
        }
    }

    // custom tag remove button
    public Locator CustomTagRemoveBtn(String CustomTag) {
        return page.locator(
                String.format("//span[text()=\"%s\"]/parent::*/svg[@data-testid=\"CancelIcon\"]/path", CustomTag));
    }

    // delete current custom tag
    public void DeleteCurrentCustomTag(List<String> CustomTags) {
        for (String CustomTag : CustomTags) {
            CustomTagRemoveBtn(CustomTag)
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            CustomTagRemoveBtn(CustomTag).click();
        }
    }

    // update station
    @SuppressWarnings("unchecked")
    public void updateStationV2(Map<String, Object> olddata, Map<String, Object> newdata) {
        System.out.println("Old data: " + olddata);
        System.out.println("New data: " + newdata);
        clickStation((String) olddata.get("stationName"));
        unCheckAllTags();
        fillStationName((String) newdata.get("stationName"));
        fillStationAngle(((Number) newdata.get("stationAngle")).doubleValue());
        fillStationXCordinate(((Number) newdata.get("xCord")).doubleValue());
        fillStationYCordinate(((Number) newdata.get("yCord")).doubleValue());
        selectTags((List<String>) newdata.get("tags"), (boolean) newdata.get("isClockwise"));
        List<String> oldCustomTags = (List<String>) olddata.get("customTags");
        if (oldCustomTags != null) {
            DeleteCurrentCustomTag(oldCustomTags);
        }
        List<String> newCustomTags = (List<String>) newdata.get("customTags");
        if (newCustomTags != null) {
            selectCustomTags(newCustomTags);
        }
        clickDoneBtn();
    }

    // get actual station details
    public Map<String, Object> getActualStationDetails(String stationName, List<String> expectedCustomTags) {
        clickStation(stationName);
        page.waitForTimeout(1000);
        Map<String, Object> actual = new HashMap<>();
        actual.put("stationName", getCurrentStationName());
        actual.put("isClockwise", getInplaceOrientation());
        actual.put("xCord", getCurrentXcord());
        actual.put("yCord", getCurrentYcord());
        actual.put("stationAngle", getCurrentStationAngle());
        actual.put("tags", getCurrentStationTags());
        actual.put("customTags", getCurrentStationCustomTags(expectedCustomTags));
        return actual;
    }

    // update station name
    public void updateStationName(String oldStationName, String newStationName) {
        clickStation(oldStationName);
        fillStationName(newStationName);
        clickDoneBtn();
        if (isStationUpdated(newStationName)) {
            System.out.println("Station :" + oldStationName + " updated to " + newStationName);
        }
    }

    // update station angle
    public void updateStationAngle(String stationName, double angle) {
        clickStation(stationName);
        fillStationAngle(angle);
        clickDoneBtn();
    }

    // update station x cordinate
    public void updateStationXCordinate(String stationName, double x) {
        clickStation(stationName);
        fillStationXCordinate(x);
        clickDoneBtn();
    }

    // update station y cordinate
    public void updateStationYCordinate(String stationName, double y) {
        clickStation(stationName);
        fillStationYCordinate(y);
        clickDoneBtn();
    }

    // update station tags
    public void updateStationTags(String stationName, List<String> tags, boolean isClockwise) {
        clickStation(stationName);
        unCheckAllTags();
        selectTags(tags, isClockwise);
        clickDoneBtn();
    }

    // update station custom tags
    public void updateStationCustomTags(String stationName, List<String> currentCustomTags,
            List<String> newCustomTags) {
        clickStation(stationName);
        DeleteCurrentCustomTag(currentCustomTags);
        selectCustomTags(newCustomTags);
        clickDoneBtn();
    }

    // update station inplace orientation
    public void updateStationInplaceOrientation(String stationName, boolean isClockwise) {
        clickStation(stationName);
        SwitchToClockwise(isClockwise);
        clickDoneBtn();
    }

    // ------------------
    // verifications
    // ------------------
    // verify created station
    @SuppressWarnings("unchecked")
    public boolean verifyCreatedStation(String stationName, Map<String, Object> expected) {
        List<String> expectedCustomTags = (List<String>) expected.get("customTags");
        Map<String, Object> actual = getActualStationDetails(stationName, expectedCustomTags);
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        boolean isVerified = true;
        for (String key : expected.keySet()) {
            Object expVal = expected.get(key);
            Object actVal = actual.get(key);

            if (expVal instanceof List && actVal instanceof List) {
                java.util.Set<Object> expSet = new java.util.HashSet<>((List<?>) expVal);
                java.util.Set<Object> actSet = new java.util.HashSet<>((List<?>) actVal);
                if (!expSet.equals(actSet)) {
                    isVerified = false;
                    logger.info("Mismatch in " + key + ": Expected " + expSet + " but got " + actSet);
                }
            } else if (expVal instanceof Number && actVal instanceof Number) {
                if (Math.abs(((Number) expVal).doubleValue() - ((Number) actVal).doubleValue()) > 0.001) {
                    isVerified = false;
                    logger.info("Mismatch in " + key + ": Expected " + expVal + " but got " + actVal);
                }
            } else if (expVal != null && !expVal.equals(actVal)) {
                isVerified = false;
                logger.info("Mismatch in " + key + ": Expected " + expVal + " but got " + actVal);
            }
        }

        if (isVerified) {
            logger.info("Station verified: " + stationName);
            return true;
        } else {
            logger.info("Station not verified: " + stationName);
            return false;
        }
    }

    // check if station is updated
    public boolean isStationUpdated(String stationName) {
        StationUpdatedSuccessDialog(stationName).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        return StationUpdatedSuccessDialog(stationName).isVisible();
    }

    // get station updated success dialog
    public Locator StationUpdatedSuccessDialog(String stationName) {
        return page.getByText("Station '" + stationName + "' updated");
    }

    // get station
    public Locator Station(String StationName) {
        return page.getByLabel(StationName, new Page.GetByLabelOptions().setExact(true))
                .getByTestId("ArrowForwardIosIcon");
    }

    // get station created success dialog
    public Locator StationCreatedSuccessDialog(String stationName) {
        return page.getByText("Station '" + stationName + "' added");
    }

    // check if station is created
    public boolean isStationCreated(String stationName) {
        StationCreatedSuccessDialog(stationName).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        return StationCreatedSuccessDialog(stationName).isVisible();
    }

    // get station deleted success dialog
    public Locator StationDeletedSuccessDialog(String stationName) {
        return page.getByText("Station " + stationName + " deleted");
    }

    // check if station is deleted
    public boolean isStationDeleted(String stationName) {
        StationDeletedSuccessDialog(stationName).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        return StationDeletedSuccessDialog(stationName).isVisible();
    }

    // get current station name
    public String getCurrentStationName() {
        return StationNameInput.inputValue();
    }

    // get current station xcord
    public double getCurrentXcord() {
        System.out.println("Current Xcord: " + XcordInput.inputValue());
        return Double.parseDouble(XcordInput.inputValue());
    }

    // get current station ycord
    public double getCurrentYcord() {
        System.out.println("Current Ycord: " + YcordInput.inputValue());
        return Double.parseDouble(YcordInput.inputValue());
    }

    // get current station angle
    public double getCurrentStationAngle() {
        System.out.println("Current Station Angle: " + stationAngleInput.inputValue());
        return Double.parseDouble(stationAngleInput.inputValue());
    }

    // check if station angle is correct
    public boolean isStationAngleCorrect(double stationAngle) {
        System.out.println("Station Angle: " + stationAngle);
        System.out.println("Current Station Angle: " + getCurrentStationAngle());
        return getCurrentStationAngle() == stationAngle;
    }

    // check if station xcord is correct
    public boolean isStationXCorrect(double xCord) {
        System.out.println("X Cord: " + xCord);
        System.out.println("Current Xcord: " + getCurrentXcord());
        return getCurrentXcord() == xCord;
    }

    // check if station ycord is correct
    public boolean isStationYCorrect(double yCord) {
        System.out.println("Y Cord: " + yCord);
        System.out.println("Current Ycord: " + getCurrentYcord());
        return getCurrentYcord() == yCord;
    }

    // get current station tags
    public List<String> getCurrentStationTags() {
        System.out.println("Current Station Tags: " + AutounhitchTag.isChecked() + " " + dispatchNotreqdTag.isChecked()
                + " " + dispatchOptionalTag.isChecked() + " " + parkingTag.isChecked() + " " + inplaceTag.isChecked());
        List<String> tags = new ArrayList<>();
        if (dispatchNotreqdTag.isChecked())
            tags.add("dispatch_not_reqd");
        if (dispatchOptionalTag.isChecked())
            tags.add("dispatch_optional");
        if (parkingTag.isChecked())
            tags.add("parking");
        if (AutounhitchTag.isChecked())
            tags.add("autounhitch");
        if (inplaceTag.isChecked())
            tags.add("inplace");
        if (ChargingStationTag.isChecked())
            tags.add("charging");
        if (HideStationTag.isChecked())
            tags.add("hidden");
        if (trolleyHitchReleaseTag.isChecked())
            tags.add("trolley_hitch_release");
        return tags;
    }

    // get custom tag
    public Locator CustomTag(String customTag) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(customTag));
    }

    // get current station custom tags
    public List<String> getCurrentStationCustomTags(List<String> expectedCustomTags) {
        List<String> customTags = new ArrayList<>();
        if (expectedCustomTags != null) {
            for (String tag : expectedCustomTags) {
                if (CustomTag(tag).isVisible()) {
                    customTags.add(tag);
                }
            }
        }
        return customTags;
    }

    // check if station tags are correct
    public boolean isStationTagsCorrect(List<String> tags) {
        System.out.println("Tags: " + tags);
        System.out.println("Current Station Tags: " + getCurrentStationTags());
        java.util.Set<String> expSet = new java.util.HashSet<>(tags);
        java.util.Set<String> actSet = new java.util.HashSet<>(getCurrentStationTags());
        return expSet.equals(actSet);
    }

    // check if station custom tags are correct
    public boolean isStationCustomTagsCorrect(List<String> customTags) {
        System.out.println("Custom Tags: " + customTags);
        System.out.println("Current Station Custom Tags: " + getCurrentStationCustomTags(customTags));
        java.util.Set<String> expSet = new java.util.HashSet<>(customTags);
        java.util.Set<String> actSet = new java.util.HashSet<>(getCurrentStationCustomTags(customTags));
        return expSet.equals(actSet);
    }

    // get inplace orientation
    public boolean getInplaceOrientation() {
        if (inplaceClockwiseOption.isVisible()) {
            return true;
        } else {
            return false;
        }
    }

    // check if in map editor
    public boolean isInMapEditor() {
        RulerBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        GridBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (GridBtn.isVisible() && RulerBtn.isVisible()) {
            return true;
        } else {
            return false;
        }
    }

    // -------------------
    // ROUTES ACTIONS
    // -------------------
    public void addNewRoute(Map<String, Object> route) {
        clickAddRouteBtn();
        selectStartStation(route.get("start_station").toString());
        selectEndStation(route.get("end_station").toString());
        if (route.get("reverse").toString().equals("true")) {
            CheckReverseCheckBox();
        } else {
            unCheckReverseCheckBox();
        }
        clickDoneBtn();
    }

    // delete route
    public void deleteRoute(String startStation, String endStation) {
        routeSelect(startStation, endStation).click();
        DeleteBtn.click();
        if (isRouteDeleteConfirmDialogVisible(startStation, endStation)) {
            DeleteConfirmDialogYesBtn.click();
        }
    }

    // select route
    private final Locator routeSelect(String startStation, String endStation) {
        return page.getByLabel(startStation + " >> " + endStation).getByText(startStation + " >> " + endStation,
                new Locator.GetByTextOptions().setExact(true));
    }

    // select station for route
    private final Locator stationSelectForRoute(String StationName) {
        return page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(StationName).setExact(true));
    }

    // click routes tab
    public void clickRoutesTab() {
        System.out.println("Clicking Routes Tab");
        RoutesTab.click();
    }

    // click add route button
    public void clickAddRouteBtn() {
        clickRoutesTab();
        AddNewRouteBtn.click();
        System.out.println("Clicked Add Route Button");
    }

    // select start station
    public void selectStartStation(String stationName) {
        StartStationInput.click();
        stationSelectForRoute(stationName).click();
        System.out.println("Selected Start Station: " + stationName);
    }

    // select end station
    public void selectEndStation(String stationName) {
        EndStationInput.click();
        stationSelectForRoute(stationName).click();
        System.out.println("Selected End Station: " + stationName);
    }

    // check reverse checkbox
    public void CheckReverseCheckBox() {
        if (ReverseCheckBox.isChecked()) {
            logger.info("Reverse checkbox is already checked");
        } else {
            ReverseCheckBox.click();
            System.out.println("Checked Reverse Checkbox");
        }
    }

    // uncheck reverse checkbox
    public void unCheckReverseCheckBox() {
        System.out.println("Unchecking Reverse Checkbox");
        if (ReverseCheckBox.isChecked()) {
            ReverseCheckBox.click();
        } else {
            logger.info("Reverse checkbox is already unchecked");
        }
    }

    // click route delete button
    public void clickRouteDeleteBtn() {
        DeleteBtn.click();
        System.out.println("Clicked Route Delete Button");
    }

    // click switch direction button
    public void clickSwitchDirectionBtn() {
        SwitchDirectionBtn.click();
        System.out.println("Clicked Switch Direction Button");
    }

    // generate mirror path
    public void GeneratateMirrorPath(String StartStation, String EndStation) {
        if (GenerateMirrorPathBtn.isVisible()) {
            GenerateMirrorPathBtn.click();
            System.out.println("Generated Mirror Path");
        } else {
            logger.info("Generate Mirror Path button is not visible");
        }
    }

    // get route create success message
    private Locator RouteCreateSuccessMsg(String StartStation) {
        return page.getByText("Route between '" + StartStation + "' to '");
    }

    // get route delete success message
    private Locator RouteDeleteSuccessMsg(String StartStation, String EndStation) {
        return page.getByText("Route from '" + StartStation + "' to '");
    }

    // get route delete confirm message
    private Locator RouteDeleteConfirmMsg(String StartStation, String EndStation) {
        return page.getByText("Are you sure you want to delete this route from '" + StartStation + "' to '"
                + EndStation + "' ?NoYes");
    }

    // check if route delete confirm dialog is visible
    public boolean isRouteDeleteConfirmDialogVisible(String StartStation, String EndStation) {
        RouteDeleteConfirmMsg(StartStation, EndStation).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (RouteDeleteConfirmMsg(StartStation, EndStation).isVisible()) {
            logger.info("Route delete confirm dialog is visible");
            return true;
        } else {
            logger.info("Route delete confirm dialog is not visible");
            return false;
        }
    }

    // get route
    private Locator Route(String StartStation, String EndStation) {
        return page.getByLabel(StartStation + " >> " + EndStation);
    }

    // check if route is created
    public boolean isRouteCreated(String StartStation, String EndStation) {
        try {
            RouteCreateSuccessMsg(StartStation).waitFor(new Locator.WaitForOptions().setTimeout(10000));
            // Add wait for the Route element itself to eliminate the race condition
            Route(StartStation, EndStation).waitFor(new Locator.WaitForOptions().setTimeout(10000));
            logger.info("Route created successfully");
            return true;
        } catch (Exception e) {
            logger.info("Route creation failed: " + e.getMessage());
            return false;
        }
    }

    // check if route is deleted
    public boolean isRouteDeleted(String StartStation, String EndStation) {
        RouteDeleteSuccessMsg(StartStation, EndStation).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (RouteDeleteSuccessMsg(StartStation, EndStation).isVisible()) {
            logger.info("Route deleted successfully");
            return true;
        } else {
            logger.info("Route deletion failed");
            return false;
        }
    }

    // ================
    // CANVAS ACTIONS
    // ================
    // single click on canvas with coordinates
    public void SingleClickOnCanvasWithCoordinates(int x, int y) {
        Canvas.click(new com.microsoft.playwright.Locator.ClickOptions().setPosition(x, y));
    }

    // double click on canvas with coordinates
    public void DoubleClickOnCanvasWithCoordinates(int x, int y) {
        Canvas.click(new com.microsoft.playwright.Locator.ClickOptions().setPosition(x, y));
        Canvas.click(new com.microsoft.playwright.Locator.ClickOptions().setPosition(x, y));
    }

    // click and drag on canvas with coordinates
    public void ClickandDragOnCanvasWithCoordinates(int Start_x, int Start_y, int End_x, int End_y) {
        com.microsoft.playwright.options.BoundingBox box = Canvas.boundingBox();
        if (box != null) {
            page.mouse().move(box.x + Start_x, box.y + Start_y);
            page.mouse().down();
            page.waitForTimeout(500);
            page.mouse().move(box.x + End_x, box.y + End_y,
                    new com.microsoft.playwright.Mouse.MoveOptions().setSteps(30));
            page.waitForTimeout(500);
            page.mouse().up();
        } else {
            Canvas.dragTo(Canvas, new com.microsoft.playwright.Locator.DragToOptions()
                    .setSourcePosition(Start_x, Start_y).setTargetPosition(End_x, End_y).setForce(true));
        }
    }

    // ================
    // GATE ACTIONS
    // ================
    // add box gate
    public void addBoxGate(Map<String, Object> data) {
        clickAddGateBtn();
        SelectBoxGateType();
        ClickandDragOnCanvasWithCoordinates((int) data.get("start_x"), (int) data.get("start_y"),
                (int) data.get("end_x"), (int) data.get("end_y"));
        fillGateName((String) data.get("name"));
        fillGateAngle((int) data.get("angle"));
        fillGateReleaseDistance((int) data.get("release_distance"));
        fillGateApplyDistance((int) data.get("apply_distance"));
        clickDoneBtn();
    }

    // fill gate angle
    public void fillGateAngle(int Angle) {
        GateAngleInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        GateAngleInput.clear();
        GateAngleInput.fill(String.valueOf(Angle));
    }

    // fill gate release distance
    public void fillGateReleaseDistance(int ReleaseDistance) {
        GateReleaseDistanceInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        GateReleaseDistanceInput.clear();
        GateReleaseDistanceInput.fill(String.valueOf(ReleaseDistance));
    }

    // fill gate apply distance
    public void fillGateApplyDistance(int ApplyDistance) {
        GateApplyDistanceInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        GateApplyDistanceInput.clear();
        GateApplyDistanceInput.fill(String.valueOf(ApplyDistance));
    }

    // click add gate button
    public void clickAddGateBtn() {
        GatesTab.click();
        AddNewGateBtn.click();
    }

    // fill gate name
    public void fillGateName(String GateName) {
        GateNameInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        GateNameInput.clear();
        GateNameInput.fill(GateName);
    }

    // select box gate type
    public void SelectBoxGateType() {
        BoxgateSelector.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        BoxgateSelector.click();
    }

    // get gate created success message
    public Locator GateCreatedSuccessMsg(String GateName) {
        return page.getByText("EZ Box '" + GateName + "' updated");
    }

    // check if box gate is created
    public boolean isBoxGateCreated(String GateName) {
        GateCreatedSuccessMsg(GateName).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (GateCreatedSuccessMsg(GateName).isVisible()) {
            logger.info("Box Gate created successfully");
            return true;
        } else {
            logger.info("Box Gate creation failed");
            return false;
        }
    }
    // ====================
    // ZONES ACTIONS
    // ====================

    // add zone
    public void addZone(BaseZone zone) {
        clickAddZoneBtn();
        System.out.println("clicking and dragging on canvas with coordinates");
        ClickandDragOnCanvasWithCoordinates(
                zone.getStartX(),
                zone.getStartY(),
                zone.getEndX(),
                zone.getEndY());
        System.out.println("Clicked and dragged on canvas with coordinates");
        selectZoneType(zone);
        fillZoneName(zone.getName());
        System.out.println("Filled zone name");
        fillZoneAngle(zone.getAngle());
        System.out.println("Filled zone angle");
        zone.fillDynamicFields(this);
        System.out.println("Filled zone dynamic fields");
        clickDoneBtn();
        System.out.println("Clicked done button");
    }

    // select zone type
    public void selectZoneType(BaseZone zone) {
        System.out.println("Selecting zone type");
        switch (zone.getZoneType()) {
            case LOW_SPEED:
                SelectLowSpeedZoneType();
                break;
            case RAMP:
                SelectRampZoneType();
                break;
        }
        System.out.println("Selected zone type");
    }

    // click zones tab
    public void ClickZonesTab() {
        System.out.println("Clicking on zones tab");
        ZonesTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ZonesTab.click();
        System.out.println("Clicked on zones tab");
    }

    // click add zone button
    public void clickAddZoneBtn() {
        ClickZonesTab();
        System.out.println("Clicking on add zone button");
        AddNewZoneBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        AddNewZoneBtn.click();
        System.out.println("Clicked on add zone button");
    }

    // fill zone name
    public void fillZoneName(String ZoneName) {
        System.out.println("Filling zone name");
        ZoneNameInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ZoneNameInput.clear();
        ZoneNameInput.fill(ZoneName);
        System.out.println("Filled zone name");
    }

    // fill zone angle
    public void fillZoneAngle(int Angle) {
        System.out.println("Filling zone angle");
        ZoneAngleInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ZoneAngleInput.clear();
        ZoneAngleInput.fill(String.valueOf(Angle));
        System.out.println("Filled zone angle");
    }

    // click zone type selector
    public void clickZoneTypeSelector() {
        System.out.println("Clicking on zone type selector");
        ZoneTypeSelector.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ZoneTypeSelector.click();
        System.out.println("Clicked on zone type selector");
    }

    // select ramp zone type
    public void SelectRampZoneType() {
        clickZoneTypeSelector();
        System.out.println("Selecting ramp zone type");
        RAMP_ZONE_SELECTOR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        RAMP_ZONE_SELECTOR.click();
        System.out.println("Selected ramp zone type");
    }

    // select low speed zone type
    public void SelectLowSpeedZoneType() {
        clickZoneTypeSelector();
        System.out.println("Selecting low speed zone type");
        LOW_SPEED_ZONE_SELECTOR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        LOW_SPEED_ZONE_SELECTOR.click();
        System.out.println("Selected low speed zone type");
    }

    // select docking zone type
    public void SelectDockingZoneType() {
        clickZoneTypeSelector();
        System.out.println("Selecting docking zone type");
        DOCKING_ZONES_SELECTOR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DOCKING_ZONES_SELECTOR.click();
        System.out.println("Selected docking zone type");
    }

    // select special camera zone type
    public void SelectSpecialCameraZoneType() {
        clickZoneTypeSelector();
        System.out.println("Selecting special camera zone type");
        SPECIAL_CAMERA_ZONE_SELECTOR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SPECIAL_CAMERA_ZONE_SELECTOR.click();
        System.out.println("Selected special camera zone type");
    }

    // fill velocity factor
    public void fillVelocityFactor(double VelocityFactor) {
        System.out.println("Filling velocity factor");
        velocityFactorInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        velocityFactorInput.clear();
        velocityFactorInput.fill(String.valueOf(VelocityFactor));
        System.out.println("Filled velocity factor");
    }

    // fill gradient angle
    public void fillGradientAngle(int GradientAngle) {
        System.out.println("Filling gradient angle");
        GradientAngleInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        GradientAngleInput.clear();
        GradientAngleInput.fill(String.valueOf(GradientAngle));
        System.out.println("Filled gradient angle");
    }

    // fill entry heading
    public void fillEntryHeading(int EntryHeading) {
        System.out.println("Filling entry heading");
        EntryHeadingInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        EntryHeadingInput.clear();
        EntryHeadingInput.fill(String.valueOf(EntryHeading));
        System.out.println("Filled entry heading");
    }

    // fill up ramp heading
    public void fillUpRampHeading(int UpRampHeading) {
        System.out.println("Filling up ramp heading");
        UpRampHeadingInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        UpRampHeadingInput.clear();
        UpRampHeadingInput.fill(String.valueOf(UpRampHeading));
        System.out.println("Filled up ramp heading");
    }

    // get zone created success message
    public Locator ZoneCreatedSuccessMgs(String zoneName) {
        return page.getByText("Zone '" + zoneName + "' added");
    }

    // click on centre of zones in canvas
    public void clickZoneFromCanvas(BaseZone zone) {
        SingleClickOnCanvasWithCoordinates(zone.getCenterX(), zone.getCenterY());
    }

    // check if zone is created
    public boolean isZoneCreated(String ZoneName) {
        System.out.println("Checking if zone is created");
        ZoneCreatedSuccessMgs(ZoneName).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (ZoneCreatedSuccessMgs(ZoneName).isVisible()) {
            logger.info("Zone '" + ZoneName + "' created successfully");
            return true;
        } else {
            logger.info("Zone '" + ZoneName + "' creation failed");
            return false;
        }
    }

    public String getCurrentZoneName() {
        return ZoneNameInput.inputValue();
    }

    // check if zone is selected
    public boolean isZoneSelected(String ZoneName) {
        System.out.println("Checking if zone is selected");
        ZoneNameInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        if (EditZoneheader.isVisible() && getCurrentZoneName().equals(ZoneName)) {
            return true;
        }
        return false;
    }

}
