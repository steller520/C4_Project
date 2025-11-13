package selenium.automation.framework.tests.SignUp;

import org.testng.annotations.Test;

import selenium.automation.framework.tests.BaseTest;

public class TestCase01 extends BaseTest {


    @Test
    public void TC01() {
        System.out.println("Executing TestCase01");
        driver.get("https://www.google.com");
        extentTest.info("Navigated to Google");
        extentTest.pass("TestCase01 Passed");
    }
}
