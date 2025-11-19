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

public class TestCase02 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase02.class);

    @Parameters({"email", "password"})
    @Test
    public void TC02(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) throws InterruptedException {
        logger.info("Executing Checkout Test Case 02 - Add Comment and Place Order");
        createTest("CHECKOUT-02");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            loginPage.openLoginPage();
            loginPage.performLogin(email, password);
            Thread.sleep(2000);
            
            ;
            // Add product to cart and navigate to checkout
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            Thread.sleep(1000);
            
            cartPage.proceedToCheckout();
            Thread.sleep(2000);
            
            // Add comment to order
            String orderComment = "Please deliver between 9 AM to 5 PM";
            checkoutPage.addCommentToOrder(orderComment);
            logger.info("Added comment to order: " + orderComment);
            
            // Click place order
            checkoutPage.clickPlaceOrder();
            Thread.sleep(2000);
            
            // Verify payment form is displayed
            boolean isPaymentFormDisplayed = checkoutPage.isPaymentFormDisplayed();
            Assert.assertTrue(isPaymentFormDisplayed, "Payment form should be displayed after placing order");
            
            getTest().pass("✓ Successfully placed order and navigated to payment");
            logger.info("✓ Order placement successful");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
