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
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CART-05: Verify quantity for product after adding to cart.
 * Asserts quantity > 0 and equals expected baseline (1).
 */
public class TestCase05 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase05.class);

    @Test
    public void TC05() {
        logger.info("Executing Cart Test Case 05 - Verify Product Quantity in Cart");
        createTest("CART-05");
        getTest().info("========== Starting Test Case: CART-05 - Verify Product Quantity ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify product quantity is displayed correctly in cart");
        
        WebDriver driver = WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        
        try {
            // Step 1: Open products
            getTest().info("Step 1: Navigating to Products Page");
            productsPage.openProductsPage();
            getTest().info("Products page loaded successfully");
            
            // Step 2: Add product #1
            getTest().info("Step 2: Adding Product to Cart (Product ID: 1)");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            
            // Step 3: View cart
            getTest().info("Step 3: Navigating to Cart Page");
            productsPage.clickViewCartLink();
            getTest().info("Cart page opened");
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("view_cart"));
            
            // Step 4: Get displayed quantity
            getTest().info("Step 4: Retrieving Product Quantity");
            int quantity = cartPage.getProductQuantity("1");
            logger.info("Product quantity in cart: " + quantity);
            getTest().info("Product ID 1 quantity in cart: " + quantity);
            
            Assert.assertTrue(quantity > 0, "Product quantity should be greater than 0");
            getTest().pass("✓ Product quantity is greater than 0");
            
            // Step 5: Assert expected quantity = 1
            getTest().info("Step 5: Verifying Specific Quantity Value");
            boolean quantityMatches = cartPage.verifyCartQuantity("1", 1);
            Assert.assertTrue(quantityMatches, "Product quantity should be 1");
            getTest().info("Expected quantity: 1, Actual quantity: " + quantity);
            
            getTest().pass("✓ Product quantity verified successfully in cart");
            getTest().info("Quantity matches expected value");
            logger.info("✓ Product quantity verified successfully");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("✗ Test failed: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            getTest().info("========== Test Case CART-05 Completed ==========");
        }
    }
}
