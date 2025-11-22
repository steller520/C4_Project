package selenium.automation.framework.tests.Login;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * LOGIN-02: Validate presence of all critical login form elements (email, password, button).
 * Scope: Visibility checks only (functional input + click verified in later cases).
 */
public class TestCase02 extends BaseTest {

    @Test
        public void TC02()  {
            System.out.println("Executing Test Case 02"); // console marker
        createTest("LOGIN-02");
        getTest().info("========== Starting Test Case: LOGIN-02 - Verify Login Page Elements ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify all login form elements are displayed and functional");
        
        WebDriver driver = WebdriverUtil.getDriver();
        
        // Step 1: Navigate to login page
        getTest().info("Step 1: Opening Login Page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        System.out.println("Opened Login Page");
        getTest().info("Login page loaded successfully");

        // Step 2: Email field visibility
        getTest().info("Step 2: Verifying Email Input Field");
        boolean emailDisplayed = loginPage.isLoginEmailInputDisplayed();
        System.out.println("Login Email Input Displayed: " + emailDisplayed);
        getTest().info("Email input field visibility: " + emailDisplayed);
        
        // Step 3: Password field visibility
        getTest().info("Step 3: Verifying Password Input Field");
        boolean passwordDisplayed = loginPage.isLoginPasswordInputDisplayed();
        System.out.println("Login Password Input Displayed: " + passwordDisplayed);
        getTest().info("Password input field visibility: " + passwordDisplayed);
        
        // Step 4: Login button visibility
        getTest().info("Step 4: Verifying Login Button");
        boolean buttonDisplayed = loginPage.isLoginButtonDisplayed();
        System.out.println("Login Button Displayed: " + buttonDisplayed);
        getTest().info("Login button visibility: " + buttonDisplayed);
        
        // Step 5: Aggregate assertion
        getTest().info("Step 5: Validating all login elements");
        boolean allElementsDisplayed = emailDisplayed && passwordDisplayed && buttonDisplayed;

        if(allElementsDisplayed) {
            getTest().pass("✓ All login elements are displayed correctly");
            getTest().info("Email Input: ✓, Password Input: ✓, Login Button: ✓");
        } else {
            getTest().fail("✗ Not all login elements are displayed");
            getTest().info("Email: " + (emailDisplayed ? "✓" : "✗") + ", Password: " + (passwordDisplayed ? "✓" : "✗") + ", Button: " + (buttonDisplayed ? "✓" : "✗"));
        }
        
        Assert.assertTrue(allElementsDisplayed, "Not all login elements are displayed");
        System.out.println("All login elements are displayed."); // assertion passed marker
        getTest().info("========== Test Case LOGIN-02 Completed ==========");
    }

}
