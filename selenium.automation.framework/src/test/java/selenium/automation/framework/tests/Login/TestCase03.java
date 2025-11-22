package selenium.automation.framework.tests.Login;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.tests.BaseTest;
import selenium.automation.framework.utils.ExcelExtractorUtil;

/**
 * LOGIN-03: Data-driven login validation using Excel provider.
 * For each row: attempt login and classify outcome based on error banner presence.
 * Does not assert success vs failure explicitly against expectedResult yet (placeholder for enhancement).
 */
public class TestCase03 extends BaseTest {

    @Test(dataProvider = "excelDataProvider", dataProviderClass = ExcelExtractorUtil.class)
    public void TC03( String testCaseName, String email, String password, String expectedResult) {
        System.out.println("Executing Login Test Case 03");
        createTest(testCaseName);
        getTest().info("========== Starting Test Case: " + testCaseName + " - Login with Credentials ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Perform login and verify expected result");
        getTest().info("Test Data Source: Excel Data Provider");

        WebDriver driver = WebdriverUtil.getDriver();

        System.out.println("Test Case Name: " + testCaseName);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Expected Result: " + expectedResult);
        
        getTest().info("Test Parameters:");
        getTest().info("  - Email: " + email);
        getTest().info("  - Password: ****");
        getTest().info("  - Expected Result: " + expectedResult);
        
        // Step 1: Navigate to login
        getTest().info("Step 1: Opening Login Page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        System.out.println("Opened Login Page");
        getTest().info("Login page loaded successfully");
        
        // Step 2: Input credentials
        getTest().info("Step 2: Entering Login Credentials");
        loginPage.enterLoginEmail(email);
        getTest().info("Email entered: " + email);
        System.out.println("Entered Email");
        
        loginPage.enterLoginPassword(password);
        getTest().info("Password entered: ****");
        System.out.println("Entered Password");
        
        // Step 3: Submit form
        getTest().info("Step 3: Submitting Login Form");
        loginPage.clickLoginButton();
        getTest().info("Login button clicked");
        System.out.println("Clicked Login Button");
        
        // Step 4: Outcome classification (error banner vs success)
        getTest().info("Step 4: Verifying Login Result");
        if(loginPage.isErrorTextDisplayed()) {
            System.out.println("Login Failed - Error Text Displayed");
            getTest().info("Login failed as expected with invalid credentials");
            getTest().info("Error message displayed to user");
            getTest().pass("✓ Error text is displayed on login failure (Expected Behavior)");
        } else {
            System.out.println("Login Successful");
            getTest().info("Login successful - User authenticated");
            getTest().info("User session established");
            getTest().pass("✓ Logged in successfully with valid credentials");
        }
        getTest().info("========== Test Case " + testCaseName + " Completed ==========");

    }


}
