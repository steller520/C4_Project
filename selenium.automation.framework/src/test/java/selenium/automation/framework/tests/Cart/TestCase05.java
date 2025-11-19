package selenium.automation.framework.tests.Cart;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase05 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase05.class);

    @Test
    public void TC05() throws InterruptedException {
        logger.info("Executing Cart Test Case 05 - Verify Product Quantity in Cart");
        createTest("CART-05");
        
        WebDriver driver = WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        
        try {
            // Navigate to products and add item to cart
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            
            // Wait for cart page to load
            Thread.sleep(1000);
            
            // Verify item quantity in cart
            int quantity = cartPage.getProductQuantity("1");
            logger.info("Product quantity in cart: " + quantity);
            
            Assert.assertTrue(quantity > 0, "Product quantity should be greater than 0");
            
            // Verify specific quantity (default is 1)
            boolean quantityMatches = cartPage.verifyCartQuantity("1", 1);
            Assert.assertTrue(quantityMatches, "Product quantity should be 1");
            
            getTest().pass("✓ Product quantity verified successfully in cart");
            logger.info("✓ Product quantity verified successfully");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
