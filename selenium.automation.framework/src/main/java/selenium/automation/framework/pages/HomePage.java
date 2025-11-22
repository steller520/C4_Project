package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import selenium.automation.framework.core.ConfigManager;

/**
 * Page Object for application Home page.
 * Provides navigation entry points to major flows: login/signup, products, cart, delete account.
 * Simplicity retained: each method performs direct single action with logging.
 */
@SuppressWarnings("null")
public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    /** Driver session */
    WebDriver driver;
    // Primary navigation locators
    private By signupLoginLink = By.xpath("//a[@href=\"/login\"]");
    private By productslink = By.partialLinkText("Products");
    private By cartLink = By.xpath("//a[@href=\"/view_cart\"]");
    private By deleteAccountLink = By.partialLinkText("Delete");

    /** Base URL for home */
    private String HomePageUrl = ConfigManager.getProperty("HomePageURL");

    /** Constructs without navigation. */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        logger.info("HomePage initialized");
    }

    /** Opens home page and maximizes window. */
    public void openHomePage() {
        logger.info("Navigating to HomePage: {}", HomePageUrl);
        driver.get(HomePageUrl);
        driver.manage().window().maximize();
        logger.info("HomePage opened and window maximized");
    }

    /** Clicks signup/login link. Returns displayed state post-click for assertion. */
    public boolean clickSignupLogin() {
        logger.info("Clicking on Signup/Login link");
        driver.findElement(signupLoginLink).click();
        boolean isDisplayed = driver.findElement(signupLoginLink).isDisplayed();
        logger.info("Signup/Login link clicked, displayed: {}", isDisplayed);
        return isDisplayed;
    }
    
    /** Clicks products link. Returns enabled state for assertion. */
    public boolean clickProducts() {
        logger.info("Clicking on Products link");
        driver.findElement(productslink).click();
        boolean isEnabled = driver.findElement(productslink).isEnabled();
        logger.info("Products link clicked, enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /** Navigates to cart page via header link. */
    public void clickCart() {
        logger.info("Clicking on Cart link");
        driver.findElement(cartLink).click();
        logger.info("Cart link clicked successfully");
    }
    /** Clicks delete account link using explicit wait for stability. */
    public void clickDeleteAccount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(deleteAccountLink)).click();
    }
}
