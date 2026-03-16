package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.example.pages.MapPage;
import org.testng.annotations.DataProvider;
import org.example.models.LowSpeedZone;
import org.example.models.RampZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.Page;

public final class testUtilities extends BaseTest {

    // Prevent instantiation
    private testUtilities() {
        throw new UnsupportedOperationException("Utility class");
    }

    // =========================
    // Generic JSON helpers
    // =========================

    private static InputStream getResourceStream(String filepath) throws IOException {
        InputStream is = testUtilities.class.getClassLoader().getResourceAsStream(filepath);
        if (is == null) {
            throw new IOException("Resource not found: " + filepath);
        }
        return is;
    }

    // =========================
    // User data helpers
    // =========================

    public static List<Map<String, String>> readUserData(String filepath) throws IOException {
        try (InputStream is = getResourceStream(filepath)) {
            String content = new String(is.readAllBytes());
            JsonArray arr = JsonParser.parseString(content).getAsJsonArray();

            List<Map<String, String>> userList = new ArrayList<>();
            for (JsonElement elem : arr) {
                JsonObject obj = elem.getAsJsonObject();
                Map<String, String> entry = new HashMap<>();
                entry.put("username", obj.get("username").getAsString());
                entry.put("password", obj.get("password").getAsString());
                userList.add(entry);
            }
            return userList;
        }
    }

    @DataProvider(name = "validUsers")
    public static Object[][] validUsersProvider() throws IOException {
        List<Map<String, String>> data = readUserData("config/users/valid_users.json");
        Object[][] result = new Object[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i).get("username");
            result[i][1] = data.get(i).get("password");
        }
        return result;
    }

    @DataProvider(name = "invalidUsers")
    public static Object[][] invalidUsersProvider() throws IOException {
        List<Map<String, String>> data = readUserData("config/users/invalid_users.json");
        Object[][] result = new Object[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i).get("username");
            result[i][1] = data.get(i).get("password");
        }
        return result;
    }

    // =========================
    // Fleet name helpers
    // =========================

    public static List<String> readFleetNames(String filepath) throws IOException {
        try (InputStream is = getResourceStream(filepath)) {
            String content = new String(is.readAllBytes());
            JsonArray arr = JsonParser.parseString(content).getAsJsonArray();

            List<String> fleetList = new ArrayList<>();
            for (JsonElement elem : arr) {
                JsonObject obj = elem.getAsJsonObject();
                fleetList.add(obj.get("fleetName").getAsString());
            }
            return fleetList;
        }
    }

    public static List<String> readNewInvalidFleetNames(String filepath) throws IOException {
        try (InputStream is = getResourceStream(filepath)) {
            String content = new String(is.readAllBytes());
            JsonArray arr = JsonParser.parseString(content).getAsJsonArray();

            List<String> fleetList = new ArrayList<>();
            for (JsonElement elem : arr) {
                JsonObject obj = elem.getAsJsonObject();
                fleetList.add(obj.get("newfleetName").getAsString());
            }
            return fleetList;
        }
    }

    @DataProvider(name = "validFleetNames")
    public static Object[][] validFleetNamesProvider() throws IOException {
        List<String> data = readFleetNames("config/fleets/valid_fleetnames.json");
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }

    @DataProvider(name = "invalidFleetNames")
    public static Object[][] invalidFleetNamesProvider() throws IOException {
        List<String> data = readFleetNames("config/fleets/invalid_fleetnames.json");
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }

    @DataProvider(name = "newInvalidFleetNames")
    public static Object[][] invalidFleetPairsProvider() throws IOException {
        List<String> oldFleetNames = readFleetNames("config/fleets/valid_fleetnames.json");
        List<String> newFleetNames = readNewInvalidFleetNames("config/fleets/New_Invalid_fleetnames.json");

        int size = Math.min(oldFleetNames.size(), newFleetNames.size());
        Object[][] data = new Object[size][2];

        for (int i = 0; i < size; i++) {
            data[i][0] = oldFleetNames.get(i); // old fleet
            data[i][1] = newFleetNames.get(i); // invalid fleet
        }

        return data;
    }
    // =========================
    // Map files helpers
    // =========================

    public static List<String> readMapFiles(String filepath) throws IOException {
        System.out.println("Loading: " + filepath);
        try (InputStream is = getResourceStream(filepath)) {
            String content = new String(is.readAllBytes());
            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            JsonArray arr = root.getAsJsonArray("mapsV2");

            List<String> result = new ArrayList<>();
            for (JsonElement element : arr) {
                result.add(element.getAsJsonObject().get("file").getAsString());
            }
            return result;
        }
    }

    @DataProvider(name = "v2mapImport")
    public static Object[][] v2MapFilesProvider() throws IOException {
        System.out.println("Running DataProvider...");
        List<String> files = readMapFiles("config/map_Files/map_names.json");
        Object[][] data = new Object[files.size()][1];
        for (int i = 0; i < files.size(); i++) {
            Path fullPath = Paths.get("src", "test", "resources", "config", "map_Files", files.get(i));
            data[i][0] = fullPath.toAbsolutePath().toString();
        }
        return data;
    }

    // =========================
    // Helpers
    // =========================

    public static final class LoginHelpers {

        private LoginHelpers() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static void loginWithFirstValidUser(Page page) throws IOException {
            List<Map<String, String>> validUsers = testUtilities.readUserData("config/users/valid_users.json");
            Map<String, String> firstUser = validUsers.get(0);

            LoginPage loginPage = new LoginPage(page);
            loginPage.login(firstUser.get("username"), firstUser.get("password"));

            page.getByText("Deployment Manager").waitFor();
            if (page.getByText("Deployment Manager").isVisible()) {
                System.out.println("Logged in successfully");
            } else {
                System.out.println("Wrong credentials, cannot login!");
                throw new RuntimeException("Login failed for first valid user");
            }
        }

        public static void cleanExistingFleetIfAny(Page page, String fleetName) {
            HomePage home = new HomePage(page);
            home.clickFleetMenu();
            if (home.searchFleet(fleetName)) {
                home.deleteExistingFleet(fleetName);
            } else {
                System.out.println("Fleet not found");
            }
        }
    }
    // =========================
    // Navigation Helpers
    // =========================

    public final static class Navigation {
        private Navigation() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static void navigateToMapEditor(Page page) throws IOException {
            LoginHelpers.loginWithFirstValidUser(page);
            HomePage home = new HomePage(page);
            home.clickMapMenu();
            MapPage map = new MapPage(page);
            if (map.isMapHeaderVisible()) {
                System.out.println("on map page successfully");

            }
            throw new RuntimeException("map page header not visible");
        }

    }

    public final static class MapDataHelpers {
        private MapDataHelpers() {
            throw new UnsupportedOperationException();
        }

        @DataProvider(name = "v2mapImportWithSearch")
        public static Object[][] v2mapImportWithSearch() throws IOException {
            List<String> mapNames = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();

            try (InputStream inSt = getResourceStream("config/map_Files/map_names.json")) {
                String content = new String(inSt.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonArray arr = root.getAsJsonArray("mapsV2");

                for (JsonElement element : arr) {
                    JsonObject mapObj = element.getAsJsonObject();
                    mapNames.add(mapObj.get("name").getAsString());
                    fileNames.add(mapObj.get("file").getAsString());
                }
            }

            Object[][] data = new Object[mapNames.size()][2];
            for (int i = 0; i < mapNames.size(); i++) {
                data[i][0] = fileNames.get(i); // File name for import
                data[i][1] = mapNames.get(i); // Map name for search verification
            }
            return data;
        }

        @DataProvider(name = "boxGateDetails")
        public static Object[][] boxGateDetails() throws IOException {
            try (InputStream is = getResourceStream("config/map_Files/Map_test_data/Map_data_provider.json")) {
                String content = new String(is.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonArray gatesInfo = root.getAsJsonArray("Box_Gates");

                Object[][] result = new Object[gatesInfo.size()][1];
                int i = 0;
                for (JsonElement element : gatesInfo) {
                    JsonObject gateObj = element.getAsJsonObject();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", gateObj.get("name").getAsString());
                    data.put("start_x", gateObj.get("start_x").getAsInt());
                    data.put("start_y", gateObj.get("start_y").getAsInt());
                    data.put("end_x", gateObj.get("end_x").getAsInt());
                    data.put("end_y", gateObj.get("end_y").getAsInt());
                    data.put("width", gateObj.get("width").getAsInt());
                    data.put("height", gateObj.get("height").getAsInt());
                    data.put("angle", gateObj.get("angle").getAsInt());
                    data.put("apply_distance", gateObj.get("apply_distance").getAsInt());
                    data.put("release_distance", gateObj.get("release_distance").getAsInt());
                    result[i][0] = data;
                    i++;
                }
                return result;
            }
        }

        private static <T> Object[][] getZonesByType(
                String zoneType,
                Function<JsonObject, T> mapper) throws IOException {

            try (InputStream is = getResourceStream("config/map_Files/Map_test_data/Map_data_provider.json")) {

                String content = new String(is.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonObject zonesObj = root.getAsJsonObject("Zones");
                JsonArray zonesArray = zonesObj.getAsJsonArray(zoneType);

                Object[][] result = new Object[zonesArray.size()][1];

                for (int i = 0; i < zonesArray.size(); i++) {
                    JsonObject zoneObj = zonesArray.get(i).getAsJsonObject();
                    result[i][0] = mapper.apply(zoneObj);
                }

                return result;
            }
        }

        @DataProvider(name = "rampZones")
        public static Object[][] rampZones() throws IOException {
            return getZonesByType("RAMP_ZONES", zoneObj -> new RampZone(
                    zoneObj.get("name").getAsString(),
                    zoneObj.get("start_x").getAsInt(),
                    zoneObj.get("start_y").getAsInt(),
                    zoneObj.get("end_x").getAsInt(),
                    zoneObj.get("end_y").getAsInt(),
                    zoneObj.get("angle").getAsInt(),
                    zoneObj.get("up_ramp_heading").getAsInt(),
                    zoneObj.get("gradient_angle").getAsInt()));
        }

        @DataProvider(name = "lowSpeedZones")
        public static Object[][] lowSpeedZones() throws IOException {
            return getZonesByType("LOW_SPEED_ZONES", zoneObj -> new LowSpeedZone(
                    zoneObj.get("name").getAsString(),
                    zoneObj.get("start_x").getAsInt(),
                    zoneObj.get("start_y").getAsInt(),
                    zoneObj.get("end_x").getAsInt(),
                    zoneObj.get("end_y").getAsInt(),
                    zoneObj.get("angle").getAsInt(),
                    zoneObj.get("velocity_factor").getAsDouble()));
        }

        // =========================
        // Station Data Helpers
        // =========================
        @DataProvider(name = "StationDetails")
        public static Object[][] stationDetailsProvider() throws IOException {
            try (InputStream is = getResourceStream("config/map_Files/Map_test_data/Map_data_provider.json")) {
                String content = new String(is.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonArray stationsInfo = root.getAsJsonArray("stations");

                Object[][] result = new Object[stationsInfo.size()][1];
                int i = 0;
                for (JsonElement element : stationsInfo) {
                    JsonObject stationObj = element.getAsJsonObject();
                    Map<String, Object> data = new HashMap<>();
                    data.put("stationName", stationObj.get("name").getAsString());
                    data.put("xCord", stationObj.get("x").getAsDouble());
                    data.put("yCord", stationObj.get("y").getAsDouble());
                    // Angle in new JSON represents degrees explicitly (0, 90, 180, etc.)
                    data.put("stationAngle", stationObj.get("angle").getAsDouble());
                    List<String> tags = new ArrayList<>();
                    if (stationObj.has("tags") && !stationObj.get("tags").isJsonNull()) {
                        JsonArray stationTags = stationObj.getAsJsonArray("tags");
                        for (JsonElement tagElem : stationTags) {
                            tags.add(tagElem.getAsString());
                        }
                    }
                    data.put("tags", tags);

                    List<String> customTags = new ArrayList<>();
                    if (stationObj.has("customTags") && !stationObj.get("customTags").isJsonNull()) {
                        JsonArray customStationTags = stationObj.getAsJsonArray("customTags");
                        for (JsonElement tagElem : customStationTags) {
                            customTags.add(tagElem.getAsString());
                        }
                    }
                    data.put("customTags", customTags);
                    boolean isClockwise = false;
                    if (stationObj.has("isClockwise") && !stationObj.get("isClockwise").isJsonNull()) {
                        isClockwise = stationObj.get("isClockwise").getAsBoolean();
                    }
                    data.put("isClockwise", isClockwise);
                    result[i][0] = data;
                    i++;
                }
                return result;
            }
        }

        @DataProvider(name = "RouteDetails")
        public static Object[][] routeDetailsProvider() throws IOException {
            try (InputStream is = getResourceStream("config/map_Files/Map_test_data/Map_data_provider.json")) {
                String content = new String(is.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonArray routesInfo = root.getAsJsonArray("routes");

                Object[][] result = new Object[routesInfo.size()][1];
                int i = 0;
                for (JsonElement element : routesInfo) {
                    JsonObject routeObj = element.getAsJsonObject();
                    Map<String, Object> data = new HashMap<>();
                    data.put("start_station", routeObj.get("start_station").getAsString());
                    data.put("end_station", routeObj.get("end_station").getAsString());
                    data.put("reverse", routeObj.get("reverse").getAsBoolean());
                    result[i][0] = data;
                    i++;
                }
                return result;
            }
        }

        // =========================
        // Helper for BaseSetup (Non-DataProvider)
        // =========================
        public static String[] getEmptyMapData() throws IOException {
            List<String> mapNames = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            try (InputStream inSt = getResourceStream("config/map_Files/map_names.json")) {
                String content = new String(inSt.readAllBytes());
                JsonObject root = JsonParser.parseString(content).getAsJsonObject();
                JsonArray arr = root.getAsJsonArray("empty_map");
                for (JsonElement element : arr) {
                    JsonObject mapObj = element.getAsJsonObject();
                    mapNames.add(mapObj.get("name").getAsString());
                    fileNames.add(mapObj.get("file").getAsString());
                }
            }
            if (mapNames.isEmpty()) {
                throw new RuntimeException("No empty_map found in config/map_names.json");
            }
            // Return first empty map config: [Filename, MapName]
            return new String[] { fileNames.get(0), mapNames.get(0) };
        }
    }

    @DataProvider(name = "v4mapImportWithSearch")
    public static Object[][] v4mapImportWithSearch() throws IOException {
        List<String> mapNames = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        try (InputStream inSt = getResourceStream("config/map_Files/map_names.json")) {
            String content = new String(inSt.readAllBytes());
            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            JsonArray arr = root.getAsJsonArray("mapsV4");

            for (JsonElement element : arr) {
                JsonObject mapObj = element.getAsJsonObject();
                mapNames.add(mapObj.get("name").getAsString());
                fileNames.add(mapObj.get("file").getAsString());
            }
        }

        Object[][] data = new Object[mapNames.size()][2];
        for (int i = 0; i < mapNames.size(); i++) {
            data[i][0] = fileNames.get(i); // File name for import
            data[i][1] = mapNames.get(i); // Map name for search verification
        }
        return data;
    }

}
