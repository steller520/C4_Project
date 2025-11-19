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

public class TestCase03 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase03.class);

    @Test
    public void TC03() {
        logger.info("Executing Cart Test Case 03");
        createTest("CART-03");

        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage prdpg = new ProductsPage(driver);

        try {
            prdpg.openProductsPage();
            logger.info("Opened Products Page");

            // Wait for page readyState and for a known product container to be present.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

            // Wait for DOM ready
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

            

            // Add first product to cart. Assume addFirstProductToCart(index) clicks and initiates UI update.
            prdpg.addFirstProductToCart(1);
            logger.info("Clicked Add to Cart on First Product");

            prdpg.clickViewCartLink();
            logger.info("Clicked View Cart Link");
            
            // Wait for cart page to load
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            Thread.sleep(1000); // Small wait for cart to render
            
            CartPage cartPage = new CartPage(driver);
            boolean isInCart = cartPage.isItemsInCart(1);
            Assert.assertTrue(isInCart, "Product with ID 1 should be in the cart.");

            // Additional asserts can be made, e.g., cart count increased, toast message text, etc.
            logger.info("Product successfully added to cart.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Test interrupted", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Error in TC03", e);
            // Re-throw to make sure test is marked failed
            throw e;
        } finally {
            
            logger.info("Completed Cart Test Case 03");
        }
    }
}
