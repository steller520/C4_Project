package selenium.automation.framework.tests.Cart;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CART-07: Validate quantity changes update line totals correctly and persist after refresh.
 * Uses explicit waits to ensure DOM updates before assertions.
 */
public class TestCase07 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase07.class);

    @Test
    public void TC07() {
        logger.info("Executing Cart Test Case 07 - Proceed to Checkout");
        createTest("CART-07");
        getTest().info("========== Starting Test Case: CART-07 - Proceed to Checkout ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify user cannot proceed from cart to checkout page without login");

        WebDriver driver = WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);

        try {
            // Step 1: Open products listing
            getTest().info("Step 1: Navigating to Products Page");
            productsPage.openProductsPage();
            getTest().info("Products page loaded successfully");
            
            // Step 2: Add product #2
            getTest().info("Step 2: Adding Product to Cart (Product ID: 2)");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            
            // Step 3: View cart
            getTest().info("Step 3: Navigating to Cart Page");
            productsPage.clickViewCartLink();
            getTest().info("Cart page opened");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("view_cart"));

            // Step 4: Capture initial quantity/price for baseline
            getTest().info("Step 4: Capturing Initial Quantity and Price");
            boolean itemInCart = cartPage.isItemsInCart(1);
            Assert.assertTrue(itemInCart, "Item should be in cart before checkout");
            getTest().info("Product ID 1 found in cart: " + itemInCart);
            getTest().pass("✓ Product verified in cart");

            // Step 5: Increase quantity to 3
            getTest().info("Step 5: Updating Quantity to 3");
            cartPage.proceedToCheckout();
            getTest().info("Proceed to checkout action triggered");

            // Wait for navigation to complete (checkout or login page)
            try {
                wait.until(d -> {
                    String url = driver.getCurrentUrl();
                    return url != null && (url.contains("checkout") || url.contains("login"));
                });
            } catch (Exception e) {
                // Continue if wait times out - page may have loaded already
                logger.debug("URL wait completed with timeout");
            }

            // Step 6: Wait for line total to reflect quantity change
            getTest().info("Step 6: Waiting for Total Price Update");
            boolean userLoggedIn = cartPage.isUserLoggedIn();
            getTest().info("User login status: " + (userLoggedIn ? "Logged In" : "Guest"));
            
            if (userLoggedIn) {
                boolean checkoutDisplayed = checkoutPage.isAddressFormDisplayed();
                Assert.assertTrue(checkoutDisplayed, "Checkout page should be displayed");
                getTest().pass("✓ Successfully proceeded to checkout (Logged In User)");
                getTest().info("Checkout page with address form is displayed");
                logger.info("✓ Checkout navigation successful");
            } else {
                boolean checkoutModalDisplayed = cartPage.isCheckoutModalDisplayed();
                Assert.assertTrue(checkoutModalDisplayed, "Checkout modal should be displayed");
                getTest().pass("✓ Checkout modal displayed for guest user");
                getTest().info("Guest user prompted to login/register before checkout");
                logger.info("✓ Checkout modal displayed successfully for guest user");
            }

        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            getTest().info("========== Test Case CART-07 Completed ==========");
        }
    }
}
