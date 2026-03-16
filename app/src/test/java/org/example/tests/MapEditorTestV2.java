package org.example.tests;

import org.example.base.MapEditorBaseTestV2;
import org.example.models.LowSpeedZone;
import org.example.pages.MapEditorV2;
import org.example.utils.testUtilities;
import org.testng.annotations.Test;
import java.util.List;
import org.testng.Assert;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import org.example.models.RampZone;
import org.testng.annotations.DataProvider;


public class MapEditorTestV2 extends MapEditorBaseTestV2 {
    // Test add new stations
    @SuppressWarnings("unchecked")
    @Test(priority = 1, dataProvider = "StationDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testAddNewStation(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        String name = (String) data.get("stationName");
        double angle = (double) data.get("stationAngle");
        double x = (double) data.get("xCord");
        double y = (double) data.get("yCord");
        List<String> tags = (List<String>) data.get("tags");
        List<String> customTags = (List<String>) data.get("customTags");
        boolean isClockwise = (data.get("isClockwise") != null) ? (boolean) data.get("isClockwise") : false;
        editor.addNewStationV2(name, angle, x, y, tags, customTags, isClockwise);
        logger.info("Station added: " + name);
        Assert.assertTrue(editor.isStationCreated(name), "Station is not created");
        logger.info("Station created: " + name);
    }

    // Test created stations
    @Test(priority = 2, dataProvider = "StationDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testCreatedStations(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        String name = (String) data.get("stationName");
        boolean isVerified = editor.verifyCreatedStation(name, data);
        logger.info("Station verified: " + name);
        Assert.assertTrue(isVerified, "Station is not verified");
        logger.info("Station verified: " + name);
    }

    // Test update stations
    @Test(priority = 3)
    public void testUpdateStation() {
        MapEditorV2 editor = new MapEditorV2(page);
        Map<String, Object> olddata = new HashMap<String, Object>();
        olddata.put("stationName", "Station10");
        olddata.put("customTags", Collections.emptyList());
        Map<String, Object> newdata = new HashMap<String, Object>();
        newdata.put("stationName", "Station20");
        newdata.put("stationAngle", 90);
        newdata.put("xCord", 10);
        newdata.put("yCord", 10);
        newdata.put("tags", List.of("autounhitch", "dispatch_not_reqd", "inplace"));
        newdata.put("customTags", List.of("Precision_docking", "parking_station", "pallet_station"));
        newdata.put("isClockwise", true);
        editor.updateStationV2(olddata, newdata);
        logger.info("Station updated");
        Assert.assertTrue(editor.isStationUpdated((String) newdata.get("stationName")), "Station is not updated");
        Assert.assertTrue(editor.verifyCreatedStation((String) newdata.get("stationName"), newdata),
                "Station is not verified");
        editor.updateStationName("Station20", "Station10");
        logger.info("Station updated Back to Station10");
    }


    // Test Create new routes
    @Test(priority = 4, dataProvider = "RouteDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testAddNewRoute(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        editor.addNewRoute(data);
        logger.info("Route added");
        Assert.assertTrue(editor.isRouteCreated((String) data.get("start_station"), (String) data.get("end_station")),
                "Route is not created");
        logger.info("Route created");
    }

    // Test delete routes
    @Test(priority = 5, dataProvider = "RouteDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testDeleteRoute(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        String startStation = (String) data.get("start_station");
        String endStation = (String) data.get("end_station");
        editor.deleteRoute(startStation, endStation);
        logger.info("Route deleted: " + startStation + " to " + endStation);
        Assert.assertTrue(editor.isRouteDeleted(startStation, endStation), "Route is not deleted");
    }

    // Test add box gate
    @Test(priority = 6, dataProvider = "boxGateDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testAddBoxGate(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        editor.addBoxGate(data);
        logger.info("Box gate added");
        Assert.assertTrue(editor.isBoxGateCreated((String) data.get("name")), "Box gate is not created");
        logger.info("Box gate created");
    }

    // Test add Low-speed-zone
   @Test(priority = 7,dataProvider = "lowSpeedZones", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testAddLowSpeedZone(LowSpeedZone zone) {
    MapEditorV2 editor = new MapEditorV2(page);
    editor.addZone(zone);
    Assert.assertTrue(editor.isZoneCreated(zone.getName()),"Low Speed Zone was not created successfully");
    logger.info("Low Speed Zone created: " + zone.getName());
}

    // Test add Ramp-zone
    @Test(priority = 8, dataProvider = "rampZones", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testAddRampZone(RampZone zone) {
        MapEditorV2 editor = new MapEditorV2(page);
        editor.addZone(zone);
        logger.info("Ramp-zone added");
        Assert.assertTrue(editor.isZoneCreated(zone.getName()), "zone is not created");
        logger.info("zone created");
    }

    @Test(priority = 9, dataProvider = "lowSpeedZones", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testSelectZonesFromCanvas(LowSpeedZone zone) {
        MapEditorV2 editor = new MapEditorV2(page);
        editor.clickZoneFromCanvas(zone);
        Assert.assertTrue(editor.isZoneSelected(zone.getName()), "Zone is not selected");
        logger.info("Clicked on zone: " + zone.getName());
    }

    // Test delete Station
    @Test(priority = 10, dataProvider = "StationDetails", dataProviderClass = testUtilities.MapDataHelpers.class)
    public void testDeleteStation(Map<String, Object> data) {
        MapEditorV2 editor = new MapEditorV2(page);
        String name = (String) data.get("stationName");
        editor.deleteStationV2(name);
        logger.info("Station deleted: " + name);
        Assert.assertTrue(editor.isStationDeleted(name), "Station is not deleted");
    }



}
