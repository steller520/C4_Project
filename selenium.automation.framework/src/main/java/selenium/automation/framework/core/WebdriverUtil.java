package selenium.automation.framework.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Utility class for WebDriver management, including initialization and cleanup.
 * This class uses ThreadLocal to ensure thread-safe WebDriver instances for parallel execution.
 */
public class WebdriverUtil {
    
    // ThreadLocal to hold WebDriver instances, ensuring thread safety
    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    /**
     * Initializes the WebDriver for the specified browser.
     * @param browserType The type of browser to initialize (e.g., "chrome", "firefox", "edge").
     * @return The initialized WebDriver instance.
     */
    public static WebDriver initializeDriver(String browserType) {
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
            case "edge":
                WebDriverManager.edgedriver().setup();
                threadLocalDriver.set(new EdgeDriver());
                break;
            default:
                System.out.println("Unsupported browser type! Defaulting to Chrome.");
                WebDriverManager.chromedriver().setup();
                threadLocalDriver.set(new ChromeDriver());
                break;
        }
        return threadLocalDriver.get();
    }

    /**
     * Quits the WebDriver and removes it from the ThreadLocal storage.
     */
    public static void quitDriver() {
        System.out.println("WebDriver quit.");
        if (threadLocalDriver.get() != null) {
            threadLocalDriver.get().quit();
            threadLocalDriver.remove(); // Clean up ThreadLocal
        }
    }

    /**
     * Gets the WebDriver instance for the current thread.
     * @return The WebDriver instance.
     */
    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    /**
     * Sets the WebDriver instance for the current thread.
     * @param driver The WebDriver instance to set.
     */
    public static void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }
}
