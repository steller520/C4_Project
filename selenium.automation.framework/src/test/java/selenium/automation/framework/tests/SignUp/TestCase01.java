package selenium.automation.framework.tests.SignUp;

import java.io.IOException;

import org.testng.annotations.Test;

import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase01 extends BaseTest {

    // Test Case 01: Verify Navigation to Signup/Login Page
    @Test
    public void TC01() throws IOException {
        // ScreenShotUtil.takeScreenshot(driver, "TC01_BeforeNavigation");
        System.out.println("Executing TestCase01");
        HomePage homePage = new HomePage(driver);
        boolean result = homePage.clickSignupLogin();
        // Assert.assertTrue(result, "Navigated to Signup/Login page successfully.");
        if(result) {
            getTest().pass("Navigated to Signup/Login page successfully.");
        } else {    
            getTest().fail("Failed to navigate to Signup/Login page.");
        }

        
        
    }
}
