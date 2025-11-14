package selenium.automation.framework.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebdriverUtil {
    static WebDriver driver = null;
    public static WebDriver initializeDriver(String browserType) {
        // Placeholder for WebDriver initialization logic
        System.out.println("WebDriver initialized.");
        switch (browserType.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                System.out.println("Unsupported browser type! Defaulting to Chrome.");
                driver = new ChromeDriver();
                break;
        }
        return driver;
    }

    public static void quitDriver() {
        // Placeholder for WebDriver quit logic
        System.out.println("WebDriver quit.");
        if (driver != null) {
            driver.quit();
        }
    }
}
