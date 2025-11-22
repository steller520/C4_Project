package selenium.automation.framework.tests.Features;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.tests.BaseTest;

/**
 * WAITS-DEMO: Compare implicit vs explicit vs fluent wait strategies.
 * Includes an intentional implicit wait fragility example and robust explicit/fluent alternatives.
 */
public class WaitsDemo extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(WaitsDemo.class);

    @Test(priority = 1)
    public void testImplicitWaitFailure() {
        logger.info("Executing Implicit Wait Failure Demo");
        createTest("WAITS-IMPLICIT-FAIL");
        getTest().info("========== Implicit Wait Failure Demo ==========");
        getTest().info("Test Objective: Show limitation of Implicit Wait with dynamic content");
        
        WebDriver driver = WebdriverUtil.getDriver();
        
        try {
            // Step 0: Configure short implicit wait (fragile scenario setup)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            getTest().info("Implicit wait set to 2 seconds");
            
            getTest().info("Step 1: Opening Products Page");
            driver.get("https://automationexercise.com/products");
            getTest().info("Products page loaded");
            
            getTest().info("Step 2: Clicking first product 'Add to Cart'");
            WebElement addToCartBtn = driver.findElement(
                By.xpath("(//a[@data-product-id='1'])[1]"));
            // Use JavaScript click to avoid ad interference
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
            getTest().info("Add to cart clicked");
            
            // Step 3: Attempt immediate modal discovery (expected fragility)
            getTest().info("Step 3: Trying to find modal with Implicit Wait");
            long startTime = System.currentTimeMillis();
            
            try {
                WebElement modal = driver.findElement(By.className("modal-content"));
                long endTime = System.currentTimeMillis();
                getTest().info("Modal found in " + (endTime - startTime) + "ms");
                
                // If we reach here, implicit wait was sufficient (but not ideal)
                getTest().warning("⚠ Modal found with implicit wait, but this is not reliable for dynamic content");
                
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                getTest().info("Modal NOT found after " + (endTime - startTime) + "ms");
                getTest().fail("✗ Implicit wait failed to find dynamic modal content");
                throw e;
            }
            
        } catch (Exception e) {
            logger.error("Expected failure with implicit wait", e);
            getTest().fail("✗ Implicit wait is insufficient for dynamic content: " + e.getMessage());
            throw e;
        } finally {
            // Reset implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            getTest().info("========== Implicit Wait Demo Completed ==========");
        }
    }

    @Test(priority = 2)
    public void testExplicitWaitSuccess() {
        logger.info("Executing Explicit Wait Success Demo");
        createTest("WAITS-EXPLICIT-SUCCESS");
        getTest().info("========== Explicit Wait Success Demo ==========");
        getTest().info("Test Objective: Show Explicit Wait handling dynamic content successfully");
        
        WebDriver driver = WebdriverUtil.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            getTest().info("Step 1: Opening Products Page");
            driver.get("https://automationexercise.com/products");
            wait.until(ExpectedConditions.urlContains("products"));
            getTest().info("Products page loaded");
            
            getTest().info("Step 2: Clicking first product 'Add to Cart'");
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//a[@data-product-id='1'])[1]")));
            // Use JavaScript click to avoid ad interference
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
            getTest().info("Add to cart clicked");
            
            // Step 3: Use explicit wait for dynamic modal visibility
            getTest().info("Step 3: Waiting for modal with Explicit Wait");
            long startTime = System.currentTimeMillis();
            
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("modal-content")));
            
            long endTime = System.currentTimeMillis();
            getTest().info("Modal found in " + (endTime - startTime) + "ms");
            
            Assert.assertNotNull(modal, "Modal should be visible");
            Assert.assertTrue(modal.isDisplayed(), "Modal should be displayed");
            getTest().pass("✓ Explicit Wait successfully handled dynamic modal content");
            
            // Verify modal content
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Continue Shopping')]")));
            Assert.assertTrue(continueBtn.isDisplayed(), "Continue button should be visible");
            getTest().pass("✓ Modal content verified");
            
            // Close modal
            continueBtn.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal-content")));
            getTest().info("Modal closed successfully");
            
            logger.info("✓ Explicit wait demo completed successfully");
            getTest().pass("✓ Explicit Wait is reliable for dynamic content");
            
        } catch (Exception e) {
            logger.error("Error in explicit wait demo", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            getTest().info("========== Explicit Wait Demo Completed ==========");
        }
    }

    @Test(priority = 3)
    public void testFluentWaitDemo() {
        logger.info("Executing Fluent Wait Demo");
        createTest("WAITS-FLUENT");
        getTest().info("========== Fluent Wait Demo ==========");
        getTest().info("Test Objective: Demonstrate Fluent Wait with custom polling and ignoring exceptions");
        
        WebDriver driver = WebdriverUtil.getDriver();
        
        try {
            getTest().info("Step 1: Opening Products Page");
            driver.get("https://automationexercise.com/products");
            getTest().info("Products page loaded");
            
            // Step 2: Configure fluent wait parameters (timeout, polling, ignored exceptions)
            getTest().info("Step 2: Configuring Fluent Wait");
            getTest().info("- Timeout: 15 seconds");
            getTest().info("- Polling interval: 500 milliseconds");
            getTest().info("- Ignoring: NoSuchElementException");
            
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .withMessage("Element not found even after fluent wait");
            
            getTest().info("Step 3: Using Fluent Wait to find dynamic element");
            long startTime = System.currentTimeMillis();
            
            WebElement searchBox = fluentWait.until(d -> {
                WebElement element = d.findElement(By.id("search_product"));
                if (element.isDisplayed()) {
                    return element;
                }
                return null;
            });
            
            long endTime = System.currentTimeMillis();
            getTest().info("Element found in " + (endTime - startTime) + "ms");
            
            Assert.assertNotNull(searchBox, "Search box should be found");
            Assert.assertTrue(searchBox.isDisplayed(), "Search box should be displayed");
            getTest().pass("✓ Fluent Wait successfully found element with custom polling");
            
            // Step 4: Use fluent wait for follow-up search results condition
            getTest().info("Step 4: Using Fluent Wait for visibility condition");
            searchBox.sendKeys("Blue Top");
            
            WebElement searchBtn = fluentWait.until(ExpectedConditions.elementToBeClickable(
                By.id("submit_search")));
            searchBtn.click();
            
            WebElement searchResult = fluentWait.until(d -> {
                WebElement result = d.findElement(By.className("features_items"));
                if (result.isDisplayed() && !result.getText().isEmpty()) {
                    return result;
                }
                return null;
            });
            
            Assert.assertTrue(searchResult.isDisplayed(), "Search results should be displayed");
            getTest().pass("✓ Fluent Wait handled search results successfully");
            
            getTest().pass("✓ Fluent Wait demo completed - shows custom polling and exception handling");
            logger.info("✓ Fluent wait demo completed");
            
        } catch (Exception e) {
            logger.error("Error in fluent wait demo", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            getTest().info("========== Fluent Wait Demo Completed ==========");
        }
    }
}
