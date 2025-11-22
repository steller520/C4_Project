package selenium.automation.framework.tests.Features;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.tests.BaseTest;

/**
 * RELATIVE-LOCATORS-DEMO: Demonstrate Selenium 4 relative locator strategies (below, above, toRightOf, toLeftOf, near).
 * Purpose: Show expressive spatial relationships without brittle absolute XPaths.
 */
public class RelativeLocatorsDemo extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(RelativeLocatorsDemo.class);

    @Test
    @SuppressWarnings("null") // Suppress static analysis nullability warnings on RelativeLocator chained By instances
    public void testRelativeLocators() {
        logger.info("Executing Relative Locators Demo Test");
        createTest("RELATIVE-LOCATORS-DEMO");
        getTest().info("========== Selenium 4 Relative Locators Demo ==========");
        getTest().info("Test Objective: Demonstrate above, below, toLeftOf, toRightOf, near");
        
        WebDriver driver = WebdriverUtil.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Step 1: Navigate to products listing
            getTest().info("Step 1: Opening Products Page");
            driver.get("https://automationexercise.com/products");
            wait.until(ExpectedConditions.urlContains("products"));
            getTest().info("Products page loaded");
            
            // Step 2: Identify anchor element for spatial queries
            getTest().info("Step 2: Locating anchor element (first product)");
            WebElement firstProductName = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[@class='productinfo text-center']//p)[1]")));
            getTest().info("Anchor element found: " + firstProductName.getText());
            
            // Step 3: Locate element below anchor
            getTest().info("Step 3: Using 'below' relative locator");
            By belowLocator = RelativeLocator.with(By.tagName("h2"))
                .below(By.xpath("(//div[@class='productinfo text-center']//p)[1]"));
            WebElement elementBelow = driver.findElement(belowLocator);
            String priceBelow = elementBelow.getText();
            getTest().info("Element below anchor: " + priceBelow);
            Assert.assertNotNull(priceBelow, "Element below should be found");
            getTest().pass("✓ 'below' relative locator successful");
            
            // Step 4: Locate element above price element
            getTest().info("Step 4: Using 'above' relative locator");
            WebElement priceElement = driver.findElement(
                By.xpath("(//div[@class='productinfo text-center']//h2)[2]"));
            By aboveLocator = RelativeLocator.with(By.tagName("p"))
                .above(priceElement);
            WebElement elementAbove = driver.findElement(aboveLocator);
            String nameAbove = elementAbove.getText();
            getTest().info("Element above price: " + nameAbove);
            Assert.assertNotNull(nameAbove, "Element above should be found");
            getTest().pass("✓ 'above' relative locator successful");
            
            // Step 5: Locate container to the right of first product
            getTest().info("Step 5: Using 'toRightOf' relative locator");
            WebElement firstProductContainer = driver.findElement(
                By.xpath("(//div[@class='col-sm-4'])[1]"));
            By rightLocator = RelativeLocator.with(By.className("col-sm-4"))
                .toRightOf(firstProductContainer);
            WebElement elementToRight = driver.findElement(rightLocator);
            Assert.assertNotNull(elementToRight, "Element to right should be found");
            getTest().pass("✓ 'toRightOf' relative locator successful");
            
            // Step 6: Locate container to the left of third product
            getTest().info("Step 6: Using 'toLeftOf' relative locator");
            WebElement thirdProductContainer = driver.findElement(
                By.xpath("(//div[@class='col-sm-4'])[3]"));
            By leftLocator = RelativeLocator.with(By.className("col-sm-4"))
                .toLeftOf(thirdProductContainer);
            WebElement elementToLeft = driver.findElement(leftLocator);
            Assert.assertNotNull(elementToLeft, "Element to left should be found");
            getTest().pass("✓ 'toLeftOf' relative locator successful");
            
            // Step 7: Locate button near search box
            getTest().info("Step 7: Using 'near' relative locator");
            WebElement searchBox = driver.findElement(By.id("search_product"));
            By nearLocator = RelativeLocator.with(By.tagName("button"))
                .near(searchBox);
            WebElement elementNear = driver.findElement(nearLocator);
            Assert.assertNotNull(elementNear, "Element near search box should be found");
            getTest().info("Element near search box: " + elementNear.getAttribute("class"));
            getTest().pass("✓ 'near' relative locator successful");
            
            getTest().pass("✓ All Selenium 4 relative locators demonstrated successfully");
            logger.info("✓ Relative locators demo completed");
            
        } catch (Exception e) {
            logger.error("Error in relative locators demo", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            getTest().info("========== Relative Locators Demo Completed ==========");
        }
    }
}
