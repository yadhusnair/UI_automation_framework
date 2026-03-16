package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    private final Page page;

    // Locators
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator submitButton;
    private final Locator maskToggle;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
        this.usernameField = page.getByRole(AriaRole.TEXTBOX,new Page.GetByRoleOptions().setName("username"));
        this.passwordField = page.getByRole(AriaRole.TEXTBOX,new Page.GetByRoleOptions().setName("Password"));
        this.submitButton = page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("submit"));
        this.maskToggle = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("toggle password visibility"));
    }

    // Actions
    public void enterUsername(String username) {
        System.out.println("Entering username...");
        usernameField.fill(username);
        System.out.println("Successfully entered username: " + username);
    }

    public void enterPassword(String password) {
        System.out.println("Entering password...");
        passwordField.fill(password);
        System.out.println("Successfully entered password.");
    }

    public void clickSubmit() {
        System.out.println("Clicking submit button...");
        submitButton.click();
        System.out.println("Clicked submit button.");
    }

    // High-level flow
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }
    public void togglePasswordMasking() {
        System.out.println("Toggling password masking...");
        maskToggle.click();
        System.out.println("Toggled password masking.");
    }
    public boolean isPasswordMasked() {
        String type = passwordField.getAttribute("type");
        return "password".equals(type);
    }
    public boolean isSubmitButtonEnabled() {
        return submitButton.isEnabled();
    }
    public void clearUsername() {
        usernameField.clear();
    }
    public void clearPassword() {
        passwordField.clear();
    }
}
