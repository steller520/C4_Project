package selenium.automation.framework.tests.Login;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase01  extends BaseTest {

    @Test
    public void TC01() throws IOException {
        // ScreenShotUtil.takeScreenshot(driver, "TC01_BeforeNavigation");
        createTest("LOGIN-01");
        getTest().info("WebDriver initialized for test: " + "LOGIN-01");
        System.out.println("Executing TestCase01");
        WebDriver driver = WebdriverUtil.getDriver();
        HomePage homePage = new HomePage(driver);
        homePage.openHomePage();
        System.out.println("Opened Home Page");
        boolean result = homePage.clickSignupLogin();
        System.out.println("Clicked Signup/Login");
        // Assert.assertTrue(result, "Navigated to Signup/Login page successfully.");
        if(result) {
            getTest().pass("Navigated to Signup/Login page successfully.");
        } else {    
            getTest().fail("Failed to navigate to Signup/Login page.");
        }

        
        
    }

}
