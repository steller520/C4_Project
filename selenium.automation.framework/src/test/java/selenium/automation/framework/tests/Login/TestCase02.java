package selenium.automation.framework.tests.Login;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase02 extends BaseTest {

    @Test
    public void TC02()  {
      System.out.println("Executing Test Case 02");
        createTest("LOGIN-02");
        getTest().info("WebDriver initialized for test: " + "LOGIN-02");
        WebDriver driver = WebdriverUtil.getDriver();
        // Navigate to login page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        System.out.println("Opened Login Page");

        System.out.println("Login Email Input Displayed: " + loginPage.isLoginEmailInputDisplayed());
        getTest().info("Checked Login Email Input Displayed");
        System.out.println("Login Password Input Displayed: " + loginPage.isLoginPasswordInputDisplayed());
        getTest().info("Checked Login Password Input Displayed");
        System.out.println("Login Button Displayed: " + loginPage.isLoginButtonDisplayed());
        getTest().info("Checked Login Button Displayed");
        boolean allElementsDisplayed = loginPage.isLoginEmailInputDisplayed() &&
                                        loginPage.isLoginPasswordInputDisplayed() &&
                                        loginPage.isLoginButtonDisplayed();

        Assert.assertTrue(allElementsDisplayed, "Not all login elements are displayed");
        getTest().info("Verified all login elements are displayed");
        System.out.println("All login elements are displayed.");
    }

}
