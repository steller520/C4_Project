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
 * CHECKOUT-01: Logged-in user adds a product then navigates to checkout.
 * Flow: Login -> Products -> Add item -> View cart -> Proceed to checkout -> Verify address form.
 */
public class TestCase01 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase01.class);

    /**
     * This test logs in, adds a product to the cart, and proceeds to checkout.
     * @param email The email address for login.
     * @param password The password for login.
     */
    @Parameters({"email", "password"})
    @Test
    public void TC01(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) {
        // Log the start of the test case
        logger.info("Executing Checkout Test Case 01 - Navigate to Checkout");
        createTest("CHECKOUT-01");
        getTest().info("========== Starting Test Case: CHECKOUT-01 - Navigate to Checkout ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify logged-in user can navigate to checkout page");
        getTest().info("Test Credentials: Email=" + email + ", Password=****");
        
        // Get the WebDriver instance
        WebDriver driver = WebdriverUtil.getDriver();
        // Initialize Page Objects
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            // Step 1: Log in as an existing user
            getTest().info("Step 1: Performing User Login");
            loginPage.openLoginPage();
            getTest().info("Login page opened");
            loginPage.performLogin(email, password);
            getTest().info("Login completed with provided credentials");
            
            // Initialize WebDriverWait for explicit waits
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Step 2: Navigate to products catalog
            getTest().info("Step 2: Navigating to Products Page");
            productsPage.openProductsPage();
            getTest().info("Products page loaded");
            
            // Step 3: Add first product to cart
            getTest().info("Step 3: Adding Product to Cart (Product ID: 1)");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            
            // Step 4: Open cart and wait for URL stabilization
            getTest().info("Step 4: Viewing Cart");
            productsPage.clickViewCartLink();
            getTest().info("Cart page opened");
            wait.until(ExpectedConditions.urlContains("view_cart"));
            
            // Step 5: Initiate checkout from cart
            getTest().info("Step 5: Proceeding to Checkout");
            cartPage.proceedToCheckout();
            getTest().info("Checkout button clicked");
            wait.until(ExpectedConditions.urlContains("checkout"));
            
            // Step 6: Assert checkout address form visibility
            getTest().info("Step 6: Verifying Checkout Page Display");
            boolean isCheckoutDisplayed = checkoutPage.isAddressFormDisplayed();
            Assert.assertTrue(isCheckoutDisplayed, "Checkout address form should be displayed");
            getTest().info("Checkout address form visibility: " + isCheckoutDisplayed);
            
            // Mark the test as passed
            getTest().pass("✓ Successfully navigated to checkout page");
            getTest().info("User can now proceed with order placement");
            logger.info("✓ Checkout page navigation successful");
            
        } catch (Exception e) {
            // Log any exceptions that occur during the test
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            // Log the completion of the test case
            getTest().info("========== Test Case CHECKOUT-01 Completed ==========");
        }
    }
}
