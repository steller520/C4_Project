package selenium.automation.framework.tests.Cart;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase01 extends BaseTest {
    
    @Test
    public void TC01() {
        System.out.println("Executing Cart Test Case 01");
        createTest("CART-01");
        getTest().info("WebDriver initialized for test: " + "CART-01");
        WebDriver driver = WebdriverUtil.getDriver();
        // Implement cart test case logic here
        HomePage hmpg = new HomePage(driver);
        hmpg.openHomePage();
        System.out.println("Opened Home Page");
        // Additional cart operations would go here
        boolean productsClicked = hmpg.clickProducts();
        System.out.println("Clicked Products page link" );
        getTest().info("Navigated to Products Page");
        if(productsClicked) {
            getTest().pass("Products page is displayed successfully.");
        } else {
            getTest().fail("Failed to display Products page.");
        }

    }
}
