package selenium.automation.framework.tests.Checkout;

import org.openqa.selenium.JavascriptExecutor;
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

public class TestCase04 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase04.class);

    @Parameters({"email", "password"})
    @Test
    public void TC04(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) throws InterruptedException {
        logger.info("Executing Checkout Test Case 04 - Complete Order with Payment");
        createTest("CHECKOUT-04");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            loginPage.openLoginPage();
            loginPage.performLogin(email, password);
            Thread.sleep(2000);
            // Add product to cart and navigate to checkout
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            Thread.sleep(1000);
            
            cartPage.proceedToCheckout();
            Thread.sleep(2000);
            
            // Add comment and place order
            checkoutPage.addCommentToOrder("Complete end-to-end test order");
            checkoutPage.clickPlaceOrder();
            Thread.sleep(2000);
            
            // Fill payment details
            checkoutPage.fillPaymentDetails(
                "John Doe",
                "4532015112830366",
                "123",
                "12",
                "2028"
            );
            
            // Submit payment
            
            checkoutPage.clickPayAndConfirm();
            Thread.sleep(3000);
            
            // Verify order placed successfully
            boolean isOrderPlaced = checkoutPage.isOrderPlaced();
            Assert.assertTrue(isOrderPlaced, "Order should be placed successfully");
            
            String confirmationText = checkoutPage.getOrderConfirmationText();
            logger.info("Order confirmation: " + confirmationText);
            
            getTest().pass("✓ Order placed successfully");
            getTest().info("Confirmation: " + confirmationText);
            logger.info("✓ Complete checkout flow successful");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
