package selenium.automation.framework.tests.Checkout;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

/**
 * CHECKOUT-02: Logged-in user adds product, proceeds to checkout, adds order comment, and reaches payment form.
 * Focus: Validating comment persistence and payment form visibility after placing order.
 */
public class TestCase02 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase02.class);

    @Parameters({"email", "password"})
    @Test
    public void TC02(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) {
        logger.info("Executing Checkout Test Case 02 - Add Comment and Place Order");
        createTest("CHECKOUT-02");
        getTest().info("========== Starting Test Case: CHECKOUT-02 - Add Comment and Place Order ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify user can add order comment and proceed to payment");
        getTest().info("Test Credentials: Email=" + email + ", Password=****");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            // Step 1: Authenticate existing user
            getTest().info("Step 1: Performing User Login");
            loginPage.openLoginPage();
            getTest().info("Login page opened");
            loginPage.performLogin(email, password);
            getTest().info("User logged in successfully");
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Step 2: Add product and view cart
            getTest().info("Step 2: Adding Product to Cart");
            productsPage.openProductsPage();
            getTest().info("Products page loaded");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            productsPage.clickViewCartLink();
            getTest().info("Navigated to cart page");
            wait.until(ExpectedConditions.urlContains("view_cart"));
            
            // Step 3: Navigate to checkout page
            getTest().info("Step 3: Proceeding to Checkout");
            cartPage.proceedToCheckout();
            getTest().info("Checkout page loaded");
            wait.until(ExpectedConditions.urlContains("checkout"));
            
            // Step 4: Add comment in order notes
            getTest().info("Step 4: Adding Comment to Order");
            String orderComment = "Please deliver between 9 AM to 5 PM";
            checkoutPage.addCommentToOrder(orderComment);
            logger.info("Added comment to order: " + orderComment);
            getTest().info("Order comment added: '" + orderComment + "'");
            
            // Step 5: Click place order and transition to payment form
            getTest().info("Step 5: Placing Order");
            checkoutPage.clickPlaceOrder();
            getTest().info("Place order button clicked");
            wait.until(ExpectedConditions.urlContains("payment"));
            
            // Step 6: Assert payment form present
            getTest().info("Step 6: Verifying Payment Form Display");
            boolean isPaymentFormDisplayed = checkoutPage.isPaymentFormDisplayed();
            Assert.assertTrue(isPaymentFormDisplayed, "Payment form should be displayed after placing order");
            getTest().info("Payment form visibility: " + isPaymentFormDisplayed);
            
            getTest().pass("✓ Successfully placed order and navigated to payment");
            getTest().info("User can now enter payment details");
            logger.info("✓ Order placement successful");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            getTest().info("========== Test Case CHECKOUT-02 Completed ==========");
        }
    }
}
