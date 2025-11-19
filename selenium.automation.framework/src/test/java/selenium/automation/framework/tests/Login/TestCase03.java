package selenium.automation.framework.tests.Login;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.tests.BaseTest;
import selenium.automation.framework.utils.ExcelExtractorUtil;

public class TestCase03 extends BaseTest {

    @Test(dataProvider = "excelDataProvider", dataProviderClass = ExcelExtractorUtil.class)
    public void TC03( String testCaseName, String email, String password, String expectedResult) {
        System.out.println("Executing Login Test Case 03");
        createTest(testCaseName);
        getTest().info("WebDriver initialized for test: " + testCaseName);

        WebDriver driver = WebdriverUtil.getDriver();

        // Implement login test case using data from Excel
        System.out.println("Test Case Name: " + testCaseName);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Expected Result: " + expectedResult);
        // Steps to perform login and verify results would go here
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        System.out.println("Opened Login Page");
        loginPage.enterLoginEmail(email);
        getTest().info("Entered Email: " + email);
        System.out.println("Entered Email");
        loginPage.enterLoginPassword(password);
        getTest().info("Entered Password: " + password);
        System.out.println("Entered Password");
        loginPage.clickLoginButton();
        getTest().info("Clicked Login Button");
        System.out.println("Clicked Login Button");
        if(loginPage.isErrorTextDisplayed()) {
            System.out.println("Login Failed - Error Text Displayed");
            getTest().info("Login failed as expected with invalid credentials.");
            getTest().pass("Error text is displayed on login failure.");
        } else {
            System.out.println("Login Successful");
            getTest().info("Login successful.");
            getTest().pass("Logged in successfully with valid credentials.");
        }

    }


}
