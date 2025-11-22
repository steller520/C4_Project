package selenium.automation.framework.pages;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.automation.framework.core.ConfigManager;

@SuppressWarnings("null")
public class LoginPage {
    /** Logger for diagnostic and audit style messages */
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    
    /** WebDriver instance used to interact with the login page */
    WebDriver driver;
        /** Base URL for the login page, externalized via properties (non-null after construction) */
        private final String loginPageUrl = Objects.requireNonNull(
            ConfigManager.getProperty("LoginPageURL"), "LoginPageURL property missing or null");

    /* =========================================================
       Locators: kept private to enforce encapsulation. Each By
       represents a stable DOM hook for element interaction.
       ========================================================= */
    private final By loginEmailInput = By.xpath("//input[@data-qa='login-email']");
    private final By loginPasswordInput = By.xpath("//input[@data-qa='login-password']"); 
    private final By errorText = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    /**
     * Constructs the page object and logs initialization. Does not navigate.
     * @param driver active WebDriver session
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        logger.info("LoginPage initialized");
    }

    /**
     * Navigates the browser to the configured login page and maximizes the window.
     * Safe to call multiple times.
     */
    public void openLoginPage() {
        logger.info("Navigating to LoginPage: {}", loginPageUrl);
        driver.get(loginPageUrl);
        driver.manage().window().maximize();
        logger.info("LoginPage opened and window maximized");
    }

    /**
     * Types the supplied email into the email input field.
     * @param email user email credential
     */
    public void enterLoginEmail(String email) {
        logger.info("Entering login email: {}", email);
        driver.findElement(loginEmailInput).sendKeys(Objects.requireNonNull(email, "email must not be null"));
        logger.debug("Login email entered successfully");
    }

    /**
     * Types the supplied password into the password input field.
     * @param password raw password (not logged for security)
     */
    public void enterLoginPassword(String password) {
        logger.info("Entering login password (masked)");
        driver.findElement(loginPasswordInput).sendKeys(Objects.requireNonNull(password, "password must not be null")); // Intentionally not logging value
        logger.debug("Login password entered successfully");
    }

    /**
     * Clicks the login button located relative to the password field.
     * Uses Selenium 4 relative locator for resilience.
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        WebElement passwordInputElement = driver.findElement(loginPasswordInput);
        WebElement loginButton = driver.findElement(with(By.tagName("button")).below(passwordInputElement));
        loginButton.click();
        logger.info("Login button clicked");
    }

    /**
     * Determines whether the error message is currently shown.
     * Uses findElements pattern to avoid throwing when absent.
     * @return true if invalid credential banner visible
     */
    public boolean isErrorTextDisplayed() {
        return !driver.findElements(errorText).isEmpty() && driver.findElement(errorText).isDisplayed();
    }

    /** @return true if email input present and displayed */
    public boolean isLoginEmailInputDisplayed() {
        return driver.findElement(loginEmailInput).isDisplayed();
    }

    /** @return true if password input present and displayed */
    public boolean isLoginPasswordInputDisplayed() {
        return driver.findElement(loginPasswordInput).isDisplayed();
    }   

    /** @return true if login button (below password) is displayed */
    public boolean isLoginButtonDisplayed() {
        WebElement passwordInputElement = driver.findElement(loginPasswordInput);
        WebElement loginButton = driver.findElement(with(By.tagName("button")).below(passwordInputElement));
        return loginButton.isDisplayed();
    }

    /**
     * Convenience workflow combining all discrete login steps.
     * @param email user email
     * @param password user password
     */
    public void performLogin(String email, String password) {
        logger.info("Performing login for user: {}", email);
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
        logger.info("Login attempt completed for user: {}", email);
    }
}
