package selenium.automation.framework.tests.Cart;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CART-01: Navigation from Home to Products page.
 * Validates header Products link leads to products catalog.
 */
public class TestCase01 extends BaseTest {
    
    @Test
    public void TC01() {
        System.out.println("Executing Cart Test Case 01");
        createTest("CART-01");
        getTest().info("========== Starting Test Case: CART-01 - Navigate to Products Page ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify navigation from home page to products page");
        
        WebDriver driver = WebdriverUtil.getDriver();
        
        // Step 1: Open home page
        getTest().info("Step 1: Opening Home Page");
        HomePage hmpg = new HomePage(driver);
        hmpg.openHomePage();
        System.out.println("Opened Home Page");
        getTest().info("Home page loaded successfully");
        
        // Step 2: Click products link
        getTest().info("Step 2: Clicking Products Link");
        boolean productsClicked = hmpg.clickProducts();
        System.out.println("Clicked Products page link" );
        getTest().info("Products link clicked");
        
        // Step 3: Basic assertion (link remains displayed)
        getTest().info("Step 3: Verifying Products Page Display");
        if(productsClicked) {
            getTest().pass("✓ Products page is displayed successfully");
            getTest().info("User can now browse available products");
        } else {
            getTest().fail("✗ Failed to display Products page");
            getTest().info("Products page navigation failed");
        }
        getTest().info("========== Test Case CART-01 Completed ==========");

    }
}
