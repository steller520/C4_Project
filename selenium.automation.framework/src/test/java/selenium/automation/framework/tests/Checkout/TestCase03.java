package selenium.automation.framework.tests.Checkout;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.model.Log;


import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CHECKOUT-03: Logged-in user proceeds to payment page and fills card data (without confirming payment).
 * Focus: Verifying all payment input fields accept provided data.
 */
public class TestCase03 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase03.class);

    @Parameters({"email", "password"})
    @Test
    public void TC03(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) {
        logger.info("Executing Checkout Test Case 03 - Fill Payment Details");
        createTest("CHECKOUT-03");
        getTest().info("========== Starting Test Case: CHECKOUT-03 - Fill Payment Details ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify user can fill payment details in checkout");
        getTest().info("Test Credentials: Email=" + email + ", Password=****");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            // Step 1: Login flow
            getTest().info("Step 1: Performing User Login");
            loginPage.openLoginPage();
            getTest().info("Login page opened");
            loginPage.performLogin(email, password);
            getTest().info("User authenticated successfully");
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Step 2: Add product then view cart
            getTest().info("Step 2: Adding Product to Cart");
            productsPage.openProductsPage();
            getTest().info("Products page loaded");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            productsPage.clickViewCartLink();
            getTest().info("Cart page opened");
            wait.until(ExpectedConditions.urlContains("view_cart"));
            
            // Step 3: Go to checkout
            getTest().info("Step 3: Navigating to Checkout");
            cartPage.proceedToCheckout();
            getTest().info("Checkout page loaded");
            wait.until(ExpectedConditions.urlContains("checkout"));
            
            // Step 4: Add order comment and transition to payment page
            getTest().info("Step 4: Adding Order Comment and Placing Order");
            checkoutPage.addCommentToOrder("Test order comment");
            getTest().info("Order comment added");
            checkoutPage.clickPlaceOrder();
            getTest().info("Place order button clicked");
            wait.until(ExpectedConditions.urlContains("payment"));
            
            // Step 5: Populate payment form fields
            getTest().info("Step 5: Filling Payment Details");
            String nameOnCard = "John Doe";
            String cardNumber = "4532015112830366";
            String cvc = "123";
            String expiryMonth = "12";
            String expiryYear = "2028";
            
            getTest().info("Payment Information:");
            getTest().info("  - Name on Card: " + nameOnCard);
            getTest().info("  - Card Number: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
            getTest().info("  - CVC: ***");
            getTest().info("  - Expiry: " + expiryMonth + "/" + expiryYear);
            
            checkoutPage.fillPaymentDetails(nameOnCard, cardNumber, cvc, expiryMonth, expiryYear);
            logger.info("Filled payment details successfully");
            getTest().info("All payment fields filled successfully");
            
            getTest().pass("✓ Payment details filled successfully");
            getTest().info("Payment form is ready for submission");
            logger.info("✓ Payment form completed");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            getTest().info("========== Test Case CHECKOUT-03 Completed ==========");
        }
    }
}
