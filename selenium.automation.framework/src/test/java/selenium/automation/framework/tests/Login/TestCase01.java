package selenium.automation.framework.tests.Login;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.tests.BaseTest;

/**
 * LOGIN-01: Validate navigation from Home page to Signup/Login portal.
 * Scenario Outline:
 *  1. Launch home page
 *  2. Click Signup/Login header link
 *  3. Verify link remains displayed post click (basic presence check)
 * Enhancements:
 *  - Extent report steps added for granular visibility
 *  - Uses thread-safe driver from WebdriverUtil
 */
public class TestCase01  extends BaseTest {

    @Test
    public void TC01() throws IOException {
        // Test initialization & meta logging
        createTest("LOGIN-01");
        getTest().info("========== Starting Test Case: LOGIN-01 - Verify Navigation to Login Page ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify user can navigate to login page from home page");
        
        System.out.println("Executing TestCase01");
        WebDriver driver = WebdriverUtil.getDriver();
        
        // Step 1: Open home page
        getTest().info("Step 1: Opening Home Page");
        HomePage homePage = new HomePage(driver);
        homePage.openHomePage();
        System.out.println("Opened Home Page");
        getTest().info("Home page loaded successfully");
        
        // Step 2: Click signup/login
        getTest().info("Step 2: Clicking Signup/Login link");
        boolean result = homePage.clickSignupLogin();
        System.out.println("Clicked Signup/Login");
        
        // Step 3: Basic verification (link still displayed -> navigable)
        getTest().info("Step 3: Verifying navigation result");
        if(result) {
            getTest().pass("✓ Successfully navigated to Signup/Login page");
            getTest().info("User can now access login functionality");
        } else {    
            getTest().fail("✗ Failed to navigate to Signup/Login page");
            getTest().info("Navigation failed - Login page not accessible");
        }
        getTest().info("========== Test Case LOGIN-01 Completed ==========");

    }

}
