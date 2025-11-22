package selenium.automation.framework.tests.Features;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.tests.BaseTest;

/**
 * ACTIONS-DEMO: Showcase Selenium Actions API (hover, click after hover, double-click, context click).
 * Purpose: Illustrate advanced user interactions beyond simple click.
 */
public class ActionsClassDemo extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ActionsClassDemo.class);

    @Test
    public void testActionsClass() {
        logger.info("Executing Actions Class Demo Test");
        createTest("ACTIONS-DEMO");
        getTest().info("========== Actions Class Demonstration ==========");
        getTest().info("Test Objective: Demonstrate hover, double-click actions");
        
        WebDriver driver = WebdriverUtil.getDriver();
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Step 1: Navigate to home page (baseline for interactions)
            getTest().info("Step 1: Opening Home Page");
            driver.get("https://automationexercise.com/");
            wait.until(d -> {
                String url = driver.getCurrentUrl();
                return url != null && url.equals("https://automationexercise.com/");
            });
            getTest().info("Home page loaded");
            
            // Step 2: Hover over Women category to reveal sub-items
            getTest().info("Step 2: Demonstrating Hover Action on Women Category");
            WebElement womenCategory = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='#Women']")));
            
            if (womenCategory != null) {
                actions.moveToElement(womenCategory).perform();
            }
            getTest().info("Hovered over Women category");
            getTest().pass("✓ Hover action successful");
            
            // Step 3: Click expanded Women category
            getTest().info("Step 3: Clicking Women Category");
            if (womenCategory != null) {
                actions.moveToElement(womenCategory).click().perform();
            }
            
            // Wait for subcategory to expand
            WebElement dressLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]")));
            boolean isDressVisible = dressLink != null && dressLink.isDisplayed();
            Assert.assertTrue(isDressVisible, "Dress subcategory should be visible after click");
            getTest().pass("✓ Click action successful - Women category expanded");
            
            // Step 4: Double-click Dress subcategory link
            getTest().info("Step 4: Demonstrating Double-Click on Dress Link");
            if (dressLink != null) {
                actions.doubleClick(dressLink).perform();
            }
            wait.until(ExpectedConditions.urlContains("category_products"));
            getTest().pass("✓ Double-click action successful - navigated to Dress category");
            
            // Step 5: Return home for right-click demonstration
            getTest().info("Step 5: Demonstrating Context Click (Right-Click)");
            driver.navigate().back();
            wait.until(d -> {
                String url = driver.getCurrentUrl();
                return url != null && url.equals("https://automationexercise.com/");
            });
            
            WebElement menCategory = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='#Men']")));
            if (menCategory != null) {
                actions.contextClick(menCategory).perform();
            }
            getTest().info("Right-clicked on Men category");
            getTest().pass("✓ Context click (right-click) executed successfully");
            
            getTest().pass("✓ All Actions class methods demonstrated successfully");
            logger.info("✓ Actions class demo completed");
            
        } catch (Exception e) {
            logger.error("Error in Actions demo", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            getTest().info("========== Actions Demo Completed ==========");
        }
    }
}
