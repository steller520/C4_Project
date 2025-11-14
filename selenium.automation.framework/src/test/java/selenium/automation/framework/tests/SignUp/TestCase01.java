package selenium.automation.framework.tests.SignUp;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;



import selenium.automation.framework.tests.BaseTest;

public class TestCase01 extends BaseTest {


    @Test
    public void TC01() throws IOException {
        // ScreenShotUtil.takeScreenshot(driver, "TC01_BeforeNavigation");
        System.out.println("Executing TestCase01");
        driver.get("https://www.google.com");
        getTest().info("Navigated to Google homepage");
        String title = driver.getTitle();
        getTest().info("Page title is: " + title);
        Assert.assertEquals(title, "Goog");
    }
}
