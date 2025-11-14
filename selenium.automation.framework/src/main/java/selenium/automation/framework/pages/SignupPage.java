package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.automation.framework.core.ConfigManager;

public class SignupPage {
// WebDriver instance
    WebDriver driver;

    // Locators
    private By nameInputSignUp = By.name("name");
    private By emailInputSignUp = By.xpath("//input[@data-qa=\"signup-email\"]");
    private By signupButton = By.xpath("//button[@data-qa=\"signup-button\"]");

    // Constructor to initialize WebDriver and open the Signup page
    public SignupPage(WebDriver driver) {
        this.driver = driver;
        String signupPageUrl = ConfigManager.getProperty("SignUpPageURL");
        driver.get(signupPageUrl);
        driver.manage().window().maximize();
    }

    // Method to check if name input for signup is displayed
    public boolean isNameInputSignUpDisplayed() {
        return driver.findElement(nameInputSignUp).isDisplayed();
    }

    // Method to enter name and email for signup
    public void enterNameForSignup(String name) {
        driver.findElement(nameInputSignUp).sendKeys(name);
    }

    // Method to check if email input for signup is displayed
    public boolean isEmailInputSignUpDisplayed() {
        return driver.findElement(emailInputSignUp).isDisplayed();
    }

    // Method to enter email for signup
    public void enterEmailForSignup(String email) {
        driver.findElement(emailInputSignUp).sendKeys(email);
    }

    // Method to check if signup button is displayed
    public boolean isSignupButtonDisplayed() {
        return driver.findElement(signupButton).isDisplayed();
    }

    // Method to check if signup button is displayed
    public boolean isSignupButtonVisible() {
        return driver.findElement(signupButton).isDisplayed();
    }

    // Method to check if signup button is enabled
    public boolean isSignupButtonEnabled() {
        return driver.findElement(signupButton).isEnabled();
    }

    // Method to click on signup button
    public void clickSignupButton() {
        driver.findElement(signupButton).click();

    }
}
