package selenium.automation.framework.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebdriverUtil {
    
    // WebDriver instance
    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    public static WebDriver initializeDriver(String browserType) {
        // Placeholder for WebDriver initialization logic
        System.out.println("WebDriver initialized.");
        switch (browserType.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                threadLocalDriver.set(new ChromeDriver());
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                threadLocalDriver.set(new FirefoxDriver());
                break;
            default:
                System.out.println("Unsupported browser type! Defaulting to Chrome.");
                threadLocalDriver.set(new ChromeDriver());
                break;
        }
        return threadLocalDriver.get();
    }

    public static void quitDriver() {
        // Placeholder for WebDriver quit logic
        System.out.println("WebDriver quit.");
        if (threadLocalDriver.get() != null) {
            threadLocalDriver.get().quit();
        }
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }
}
