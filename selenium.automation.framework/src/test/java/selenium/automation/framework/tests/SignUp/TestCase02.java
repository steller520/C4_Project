package selenium.automation.framework.tests.SignUp;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * REG-02: Validate visibility of mandatory signup form elements (Name, Email, Signup button).
 * Flow: Open signup page -> Check each field -> Report individual pass/fail.
 */
public class TestCase02 extends BaseTest {

    // Scenario: Elements presence validation.
    @Test
    public void TC02() {
        System.out.println("Executing Test Case 02");
        createTest("REG-02");
        getTest().info("========== Starting Test Case: REG-02 - Verify Elements on Signup Page ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify all signup form elements are displayed correctly");
        
        WebDriver driver = selenium.automation.framework.core.WebdriverUtil.getDriver();
        
        // Step 1: Open signup page
        getTest().info("Step 1: Opening Signup Page");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.openSignupPage();
        System.out.println("Opened Signup Page");
        getTest().info("Signup page loaded successfully");
        
        // Step 2: Check name field presence
        getTest().info("Step 2: Verifying Name Input Field");
        boolean isNameInputDisplayed = signupPage.isNameInputSignUpDisplayed();
        System.out.println("Checked Name Input Displayed");
        getTest().info("Name input field visibility: " + isNameInputDisplayed);
        
        // Step 3: Check email field presence
        getTest().info("Step 3: Verifying Email Input Field");
        boolean isEmailInputDisplayed = signupPage.isEmailInputSignUpDisplayed();
        System.out.println("Checked Email Input Displayed");
        getTest().info("Email input field visibility: " + isEmailInputDisplayed);
        
        // Step 4: Check signup button presence
        getTest().info("Step 4: Verifying Signup Button");
        boolean isSignupButtonDisplayed = signupPage.isSignupButtonDisplayed();
        System.out.println("Checked Signup Button Displayed");
        getTest().info("Signup button visibility: " + isSignupButtonDisplayed);
        
        // Step 5: Aggregate results for reporting
        getTest().info("Step 5: Validating all elements on Signup Page");
        if(isNameInputDisplayed) {
            getTest().pass("✓ Name input field for signup is displayed");
        } else {
            getTest().fail("✗ Name input field for signup is NOT displayed");
        }

        if(isEmailInputDisplayed) {
            getTest().pass("✓ Email input field for signup is displayed");
        } else {
            getTest().fail("✗ Email input field for signup is NOT displayed");
        }

        if(isSignupButtonDisplayed) {
            getTest().pass("✓ Signup button is displayed");
        } else {
            getTest().fail("✗ Signup button is NOT displayed");
        }
        
        int passedElements = (isNameInputDisplayed ? 1 : 0) + (isEmailInputDisplayed ? 1 : 0) + (isSignupButtonDisplayed ? 1 : 0);
        getTest().info("Total elements verified: 3, Passed: " + passedElements);
        getTest().info("========== Test Case REG-02 Completed ==========");
        
        
    }
}
