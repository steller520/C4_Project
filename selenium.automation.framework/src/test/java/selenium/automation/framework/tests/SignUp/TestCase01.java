package selenium.automation.framework.tests.SignUp;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * REG-01: Navigate from Home page to Signup/Login page and verify landing.
 * Flow: Open home -> Click Signup/Login -> Assert destination components present.
 */
public class TestCase01 extends BaseTest {

    // Scenario: Basic navigation to signup/login portal.
    @Test
    public void TC01() throws IOException {
        // ScreenShotUtil.takeScreenshot(driver, "TC01_BeforeNavigation");
        createTest("REG-01");
        getTest().info("========== Starting Test Case: REG-01 - Verify Navigation to Signup/Login Page ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify user can navigate from home page to signup/login page");
        
        System.out.println("Executing TestCase01");
        WebDriver driver = WebdriverUtil.getDriver();
        
        // Step 1: Open home page
        getTest().info("Step 1: Opening Home Page");
        HomePage homePage = new HomePage(driver);
        homePage.openHomePage();
        System.out.println("Opened Home Page");
        getTest().info("Home page opened successfully");
        
        // Step 2: Click signup/login link
        getTest().info("Step 2: Clicking on Signup/Login link");
        boolean result = homePage.clickSignupLogin();
        System.out.println("Clicked Signup/Login");
        

        SignupPage signupPage = new SignupPage(driver);

        boolean isSignupLoginDisplayed = signupPage.isSignupPageDisplayed();
        System.out.println("Signup/Login Page Displayed: " + isSignupLoginDisplayed);
        
        // Step 3: Verify navigation outcome
        getTest().info("Step 3: Verifying navigation to Signup/Login page");
        if(result && isSignupLoginDisplayed) {
            getTest().pass("✓ Successfully navigated to Signup/Login page");
            getTest().info("Navigation result: SUCCESS");
        } else {    
            getTest().fail("✗ Failed to navigate to Signup/Login page");
            getTest().info("Navigation result: FAILURE");
        }
        getTest().info("========== Test Case REG-01 Completed ==========");

        
        
    }
}
