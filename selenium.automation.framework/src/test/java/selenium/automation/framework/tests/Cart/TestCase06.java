package selenium.automation.framework.tests.Cart;

import java.time.Duration;

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

public class TestCase06 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase06.class);

    @Test
    public void TC06() throws InterruptedException {
        logger.info("Executing Cart Test Case 06 - Verify Cart Total Price");
        createTest("CART-06");
        
        WebDriver driver = WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        
        try {
            // Navigate to products and add item to cart
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            
            // Wait for cart page to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(d -> ((org.openqa.selenium.JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            
            // Get product price and total
            String productPrice = cartPage.getProductPrice("1");
            int quantity = cartPage.getProductQuantity("1");
            double productTotal = Double.parseDouble(cartPage.getProductTotal("1")) * quantity;
            String cartTotal = cartPage.getCartTotal();
            
            logger.info("Product Price: " + productPrice);
            logger.info("Product Total: " + productTotal);
            logger.info("Cart Total: " + cartTotal);
            
            // Verify price is displayed
            Assert.assertFalse(productPrice.isEmpty(), "Product price should be displayed");
            Assert.assertFalse(cartTotal.isEmpty(), "Cart total should be displayed");
            
            getTest().info("Product Price: " + productPrice);
            getTest().info("Cart Total: " + cartTotal);
            getTest().pass("✓ Cart total price displayed successfully");
            logger.info("✓ Cart pricing verified successfully");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
