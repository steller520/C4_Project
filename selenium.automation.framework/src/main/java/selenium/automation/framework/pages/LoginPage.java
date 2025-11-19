package selenium.automation.framework.pages;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import selenium.automation.framework.core.ConfigManager;

public class LoginPage {
    WebDriver driver;
    private String  loginPageUrl = ConfigManager.getProperty("LoginPageURL");

    private By loginEmailInput = By.xpath("//input[@data-qa='login-email']");
    private By loginPasswordInput = By.xpath("//input[@data-qa='login-password']"); 
    private By errorText = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Open Login Page
    public void openLoginPage() {
        
        driver.get(loginPageUrl);
        driver.manage().window().maximize();
    }

    public void enterLoginEmail(String email) {
        driver.findElement(loginEmailInput).sendKeys(email);
    }

    public void enterLoginPassword(String password) {
        driver.findElement(loginPasswordInput).sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement passwordInputElement = driver.findElement(loginPasswordInput);
        WebElement loginButton = driver.findElement(with(By.tagName("button")).below(passwordInputElement));
        loginButton.click();

    }

    public boolean isErrorTextDisplayed() {
        // Use findElements to avoid NoSuchElementException when error isn't present
        return !driver.findElements(errorText).isEmpty() && driver.findElement(errorText).isDisplayed();
    }

    public boolean isLoginEmailInputDisplayed() {
        return driver.findElement(loginEmailInput).isDisplayed();
    }

    public boolean isLoginPasswordInputDisplayed() {
        return driver.findElement(loginPasswordInput).isDisplayed();
    }   

    public boolean isLoginButtonDisplayed() {
        WebElement passwordInputElement = driver.findElement(loginPasswordInput);
        WebElement loginButton = driver.findElement(with(By.tagName("button")).below(passwordInputElement));
        return loginButton.isDisplayed();
    }

    public void performLogin(String email, String password) {
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
    }
    
}
