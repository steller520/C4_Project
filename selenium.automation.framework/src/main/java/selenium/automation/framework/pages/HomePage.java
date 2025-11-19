package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import selenium.automation.framework.core.ConfigManager;

;

public class HomePage {
    // WebDriver instance
    WebDriver driver;
    // Locators
    private By signupLoginLink = By.xpath("//a[@href=\"/login\"]");
    private By productslink = By.partialLinkText("Products");
    private By cartLink = By.xpath("//a[@href=\"/view_cart\"]");
    private By deleteAccountLink = By.partialLinkText("Delete");


    // URL of the homepage
    private String HomePageUrl = ConfigManager.getProperty("HomePageURL");

    // Constructor to initialize WebDriver and open the homepage
    public HomePage(WebDriver driver) {
        this.driver = driver;
        
    }

    // Method to open the homepage
    public void openHomePage() {
        driver.get(HomePageUrl);
        driver.manage().window().maximize();
    }

    // Method to click on Signup/Login link
    public boolean clickSignupLogin() {
        driver.findElement(signupLoginLink).click();
        return driver.findElement(signupLoginLink).isDisplayed();
        
    }
    // Method to click on Products link
    public boolean clickProducts() {
        driver.findElement(productslink).click();
        return driver.findElement(productslink).isEnabled();
    }
    // Method to click on Cart link
    public void clickCart() {
        driver.findElement(cartLink).click();
    }
    // Method to click on Delete Account link
    public void clickDeleteAccount() {
        // Wait briefly for the Delete link to be present/clickable to avoid flakiness
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(deleteAccountLink)).click();
    }
}
