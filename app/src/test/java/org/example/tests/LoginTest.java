package org.example.tests;

import org.example.base.BaseLoginTest;
import org.example.pages.LoginPage;
import org.example.utils.testUtilities; // DataProviders live here
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseLoginTest { // Inherit everything from BaseLoginTest

    @AfterMethod
    public void afterEachtest() throws InterruptedException {
        Thread.sleep(2000);
    }
    //Test password masking toggle
    @Test(priority = 1)
    public void testPasswordMaskingToggle() {
        LoginPage login = new LoginPage(page);
        login.enterPassword("MySecretPassword");
        Assert.assertTrue(login.isPasswordMasked(), "Password should be masked initially");
        login.togglePasswordMasking();
        Assert.assertFalse(login.isPasswordMasked(), "Password should be unmasked after toggling");

    }
    
    //Test valid login
    @Test(dataProvider = "validUsers", dataProviderClass = testUtilities.class)
    public void testValidLogin(String username, String password) {
        LoginPage login = new LoginPage(page);
        login.login(username, password);
        // Validation: Deployment Manager text is visible after successful login
        Assert.assertTrue(page.getByText("Deployment Manager").isVisible(),
                "Deployment Manager should be visible after login");
        // Or with Playwright's assertions:
        // assertThat(page.getByText("Deployment Manager")).isVisible();
    }

    //Test invalid login
    @Test(dataProvider = "invalidUsers", dataProviderClass = testUtilities.class)
    public void testInvalidLogin(String username, String password) {
        LoginPage login = new LoginPage(page); // Uses the same inherited 'page'
        login.login(username, password);
        // Wait for the alert to appear with a timeout
        try {
            page.locator("role=alert").waitFor();
            boolean alertVisible = page.locator("role=alert").isVisible();
            Assert.assertTrue(alertVisible, "Alert should be visible for invalid login");

            // Check that alert contains error-related text (case-insensitive)
            String alertText = page.locator("role=alert").textContent();
            Assert.assertNotNull(alertText, "Alert text should not be null");
            Assert.assertTrue(alertText.toLowerCase().contains("invalid") || alertText.toLowerCase().contains("error"),
                    "Alert should contain 'invalid' or 'error': " + alertText);
        } catch (Exception e) {
            Assert.fail("Invalid login should show an alert or error message. Error: " + e.getMessage());
        }
    }

    //Test submit button disabled when any field empty
    @Test(priority = 2)
    public void testSubmitButtonDisabledWhenAnyFieldEmpty() {
        LoginPage login = new LoginPage(page);

        // Case 1: both empty
        login.clearUsername();
        login.clearPassword();
        Assert.assertFalse(
                login.isSubmitButtonEnabled(),
                "Submit button should be disabled when both fields are empty.");

        // Case 2: username filled, password empty
        login.enterUsername("someUser");
        login.clearPassword();
        Assert.assertFalse(
                login.isSubmitButtonEnabled(),
                "Submit button should be disabled when password is empty.");

        // Case 3: username empty, password filled
        login.clearUsername();
        login.enterPassword("somePassword");
        Assert.assertFalse(
                login.isSubmitButtonEnabled(),
                "Submit button should be disabled when username is empty.");

        // Case 4: both filled -> enabled
        login.enterUsername("someUser");
        login.enterPassword("somePassword");
        Assert.assertTrue(
                login.isSubmitButtonEnabled(),
                "Submit button should be enabled when both fields are filled.");
    }

}
