package selenium.automation.framework.tests.Checkout;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
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
 * CHECKOUT-04: Full end-to-end checkout including payment submission and order confirmation assertion.
 * Flow: Login -> Add product -> Checkout -> Comment -> Payment -> Confirm -> Verify order message.
 */
public class TestCase04 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase04.class);

    @Parameters({"email", "password"})
    @Test
    public void TC04(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) {
        logger.info("Executing Checkout Test Case 04 - Complete Order with Payment");
        createTest("CHECKOUT-04");
        getTest().info("========== Starting Test Case: CHECKOUT-04 - Complete Order with Payment ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify complete end-to-end order placement with payment");
        getTest().info("Test Credentials: Email=" + email + ", Password=****");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            // Step 1: User authentication
            getTest().info("Step 1: Performing User Login");
            loginPage.openLoginPage();
            getTest().info("Login page opened");
            loginPage.performLogin(email, password);
            getTest().info("User authenticated successfully");
            
            // Wait for page to load after login
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Defensive readyState wait to avoid potential null from executeScript
            wait.until(d -> {
                Object state = ((JavascriptExecutor) d).executeScript("return document.readyState");
                return state != null && "complete".equals(state.toString());
            });
            
            // Step 2: Add product and navigate to cart
            getTest().info("Step 2: Adding Product to Cart");
            productsPage.openProductsPage();
            getTest().info("Products page loaded");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            productsPage.clickViewCartLink();
            getTest().info("Cart page opened");
            
            // Wait for cart page to load
            wait.until(ExpectedConditions.urlContains("view_cart"));
            
            // Step 3: Proceed to checkout
            getTest().info("Step 3: Proceeding to Checkout");
            cartPage.proceedToCheckout();
            getTest().info("Checkout page loaded with address details");
            
            // Wait for checkout page to load
            wait.until(ExpectedConditions.urlContains("checkout"));
            
            // Step 4: Add order comment & place order
            getTest().info("Step 4: Adding Order Comment and Placing Order");
            String orderComment = "Complete end-to-end test order";
            checkoutPage.addCommentToOrder(orderComment);
            getTest().info("Order comment: '" + orderComment + "'");
            checkoutPage.clickPlaceOrder();
            getTest().info("Order placement initiated");
            
            // Wait for payment form to load
            wait.until(ExpectedConditions.urlContains("payment"));
            
            // Step 5: Enter payment details
            getTest().info("Step 5: Filling Payment Details");
            String nameOnCard = "John Doe";
            String cardNumber = "4532015112830366";
            String cvc = "123";
            String expiryMonth = "12";
            String expiryYear = "2028";
            
            getTest().info("Payment Card Details:");
            getTest().info("  - Cardholder: " + nameOnCard);
            getTest().info("  - Card: **** **** **** " + cardNumber.substring(12));
            getTest().info("  - Expiry: " + expiryMonth + "/" + expiryYear);
            
            checkoutPage.fillPaymentDetails(nameOnCard, cardNumber, cvc, expiryMonth, expiryYear);
            getTest().info("Payment details entered successfully");
            
            // Step 6: Submit payment
            getTest().info("Step 6: Submitting Payment");
            checkoutPage.clickPayAndConfirm();
            getTest().info("Payment submission triggered");
            
            // Wait for order confirmation (handled in isOrderPlaced method with 20s wait)
            
            // Step 7: Validate confirmation content
            getTest().info("Step 7: Verifying Order Confirmation");
            boolean isOrderPlaced = checkoutPage.isOrderPlaced();
            Assert.assertTrue(isOrderPlaced, "Order should be placed successfully");
            getTest().info("Order placement status: " + (isOrderPlaced ? "SUCCESS" : "FAILED"));
            
            String confirmationText = checkoutPage.getOrderConfirmationText();
            logger.info("Order confirmation: " + confirmationText);
            getTest().info("Order Confirmation Message: " + confirmationText);
            
            getTest().pass("✓ Order placed successfully - Complete E2E flow");
            getTest().info("Transaction completed successfully");
            getTest().info("User will receive order confirmation");
            logger.info("✓ Complete checkout flow successful");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error occurred during checkout process");
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            getTest().info("========== Test Case CHECKOUT-04 Completed ==========");
        }
    }
}
