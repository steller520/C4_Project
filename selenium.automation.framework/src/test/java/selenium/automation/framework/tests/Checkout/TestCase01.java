package selenium.automation.framework.tests.Checkout;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase01 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase01.class);

    @Parameters({"email", "password"})
    @Test
    public void TC01(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) throws InterruptedException {
        logger.info("Executing Checkout Test Case 01 - Navigate to Checkout");
        createTest("CHECKOUT-01");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            // First login to the application
            loginPage.openLoginPage();
            loginPage.performLogin(email, password);
            Thread.sleep(2000);
            
            // Add product to cart
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            Thread.sleep(1000);
            
            // Navigate to checkout
            cartPage.proceedToCheckout();
            Thread.sleep(2000);
            
            // Verify checkout page is displayed
            boolean isCheckoutDisplayed = checkoutPage.isAddressFormDisplayed();
            Assert.assertTrue(isCheckoutDisplayed, "Checkout address form should be displayed");
            
            getTest().pass("✓ Successfully navigated to checkout page");
            logger.info("✓ Checkout page navigation successful");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
