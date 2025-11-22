package selenium.automation.framework.tests.Cart;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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
 * CART-03: Add product then verify its presence on cart page.
 * Includes DOM readiness waits and URL confirmation for cart navigation.
 */
public class TestCase03 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase03.class);

    @Test
    public void TC03() {
        logger.info("Executing Cart Test Case 03");
        createTest("CART-03");
        getTest().info("========== Starting Test Case: CART-03 - Verify Product in Cart ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Add product to cart and verify it appears in cart page");

        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage prdpg = new ProductsPage(driver);

        try {
            // Step 1: Navigate to products page
            getTest().info("Step 1: Opening Products Page");
            prdpg.openProductsPage();
            logger.info("Opened Products Page");
            getTest().info("Products page loaded successfully");

            // Wait for page readyState and for a known product container to be present.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

            getTest().info("Waiting for page to be fully loaded");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            getTest().info("Page DOM is ready");

            // Step 2: Add product id 1
            getTest().info("Step 2: Adding First Product to Cart (Product ID: 1)");
            prdpg.addFirstProductToCart(1);
            logger.info("Clicked Add to Cart on First Product");
            getTest().info("Product added to cart successfully");

            // Step 3: Open cart via modal link
            getTest().info("Step 3: Navigating to Cart Page");
            prdpg.clickViewCartLink();
            logger.info("Clicked View Cart Link");
            getTest().info("'View Cart' link clicked");
            
            // Wait for cart page to load
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            try {
                wait.until(d -> {
                    String url = driver.getCurrentUrl();
                    return url != null && url.contains("view_cart");
                });
            } catch (Exception e) {
                // Continue if wait times out
                logger.debug("Cart page URL wait completed");
            }
            getTest().info("Cart page loaded completely");
            
            // Step 4: Validate cart row presence
            getTest().info("Step 4: Verifying Product Exists in Cart");
            CartPage cartPage = new CartPage(driver);
            boolean isInCart = cartPage.isItemsInCart(1);
            Assert.assertTrue(isInCart, "Product with ID 1 should be in the cart.");
            getTest().info("Product ID 1 verification result: " + (isInCart ? "Found" : "Not Found"));

            getTest().pass("✓ Product with ID 1 successfully verified in cart");
            getTest().info("Cart displays the added product correctly");
            logger.info("Product successfully added to cart.");

        } catch (Exception e) {
            logger.error("Error in TC03", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            logger.info("Completed Cart Test Case 03");
            getTest().info("========== Test Case CART-03 Completed ==========");
        }
    }
}
