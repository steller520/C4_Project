package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.automation.framework.core.ConfigManager;

;

public class HomePage {
    // WebDriver instance
    WebDriver driver;
    // Locators
    private By signupLoginLink = By.xpath("//a[@href=\"/login\"]");
    private By productslink = By.linkText(" Products");
    private By cartLink = By.xpath("//a[@href=\"/view_cart\"]");


    // URL of the homepage
    private String HomePageUrl = ConfigManager.getProperty("HomePageURL");

    // Constructor to initialize WebDriver and open the homepage
    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(HomePageUrl);
        driver.manage().window().maximize();
    }
    // Method to click on Signup/Login link
    public boolean clickSignupLogin() {
        driver.findElement(signupLoginLink).click();
        return driver.findElement(signupLoginLink).isDisplayed();
        
    }
    // Method to click on Products link
    public void clickProducts() {
        driver.findElement(productslink).click();
    }
    // Method to click on Cart link
    public void clickCart() {
        driver.findElement(cartLink).click();
    }
}
