package selenium.automation.framework.tests.Cart;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CART-02: Add first product to cart and confirm modal.
 * Steps: open products -> wait ready -> add product -> verify confirmation.
 */
public class TestCase02 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase02.class);

    @Test
    public void TC02() {
        logger.info("Executing Cart Test Case 02");
        createTest("CART-02");
        getTest().info("========== Starting Test Case: CART-02 - Add Product to Cart ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify product can be added to cart successfully");

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

            // Wait for DOM ready
            getTest().info("Waiting for page to be fully loaded");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            getTest().info("Page DOM is ready");

            // Step 2: Add product id 1
            getTest().info("Step 2: Adding First Product to Cart (Product ID: 1)");
            prdpg.addFirstProductToCart(1);
            logger.info("Clicked Add to Cart on First Product");
            getTest().info("'Add to Cart' button clicked for Product #1");

            // Step 3: Validate confirmation visibility
            getTest().info("Step 3: Verifying Product Added to Cart");
            boolean added = wait.until(d -> prdpg.isProductAddedToCart());
            Assert.assertTrue(added, "Product was not added to cart as expected.");
            getTest().info("Product addition confirmation received");

            getTest().pass("✓ Product successfully added to cart");
            getTest().info("Cart now contains the selected product");
            logger.info("Product successfully added to cart.");

        } catch (Exception e) {
            logger.error("Error in TC02", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            getTest().info("Error details logged for debugging");
            throw e;
        } finally {
            logger.info("Completed Cart Test Case 02");
            getTest().info("========== Test Case CART-02 Completed ==========");
        }
    }
}

