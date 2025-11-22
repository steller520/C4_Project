package selenium.automation.framework.tests.Features;

import java.time.Duration;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.tests.BaseTest;

/**
 * CONSOLE-LOGS-DEMO: Capture and classify browser console entries (SEVERE, WARNING, INFO).
 * Purpose: Surface client-side issues early in automated runs.
 */
public class BrowserConsoleLogsDemo extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BrowserConsoleLogsDemo.class);

    @Test
    public void testBrowserConsoleLogs() {
        logger.info("Executing Browser Console Logs Demo");
        createTest("CONSOLE-LOGS-DEMO");
        getTest().info("========== Browser Console Logs Demo ==========");
        getTest().info("Test Objective: Capture and display browser console errors and warnings");
        
        WebDriver driver = WebdriverUtil.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Step 1: Load home page for baseline logs
            getTest().info("Step 1: Navigating to test website");
            driver.get("https://automationexercise.com/");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("features_items")));
            getTest().info("Website loaded");
            
            // Step 2: Navigate deeper to generate potential additional logs
            getTest().info("Step 2: Navigating to Products page");
            driver.get("https://automationexercise.com/products");
            wait.until(ExpectedConditions.urlContains("products"));
            getTest().info("Navigated to products page");
            
            // Step 3: Retrieve and iterate console log entries
            getTest().info("Step 3: Capturing Browser Console Logs");
            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
            
            int severeCount = 0;
            int warningCount = 0;
            int infoCount = 0;
            
            getTest().info("Analyzing " + logEntries.getAll().size() + " console log entries:");
            
            for (LogEntry entry : logEntries) {
                String logMessage = entry.getMessage();
                
                // Count by severity
                if (entry.getLevel().equals(Level.SEVERE)) {
                    severeCount++;
                    getTest().warning("❌ SEVERE: " + logMessage);
                    logger.error("Browser Console SEVERE: " + logMessage);
                } else if (entry.getLevel().equals(Level.WARNING)) {
                    warningCount++;
                    getTest().warning("⚠ WARNING: " + logMessage);
                    logger.warn("Browser Console WARNING: " + logMessage);
                } else if (entry.getLevel().equals(Level.INFO)) {
                    infoCount++;
                    logger.info("Browser Console INFO: " + logMessage);
                }
            }
            
            getTest().info("Console Log Summary:");
            getTest().info("- SEVERE errors: " + severeCount);
            getTest().info("- WARNINGS: " + warningCount);
            getTest().info("- INFO messages: " + infoCount);
            getTest().info("- Total logs: " + logEntries.getAll().size());
            
            if (severeCount > 0) {
                getTest().warning("⚠ Found " + severeCount + " severe console errors");
            } else {
                getTest().pass("✓ No severe console errors found");
            }
            
            if (warningCount > 0) {
                getTest().info("Found " + warningCount + " console warnings (may be acceptable)");
            }
            
            getTest().pass("✓ Successfully captured and analyzed browser console logs");
            logger.info("✓ Browser console logs demo completed");
            
        } catch (Exception e) {
            logger.error("Error in browser console logs demo", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            getTest().info("========== Console Logs Demo Completed ==========");
        }
    }
}
