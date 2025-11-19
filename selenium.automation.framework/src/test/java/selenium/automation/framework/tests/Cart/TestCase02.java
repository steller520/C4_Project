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

public class TestCase02 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase02.class);

    @Test
    public void TC02() {
        logger.info("Executing Cart Test Case 02");
        createTest("CART-02");

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

            // Wait for a product-added confirmation from page object
            boolean added = wait.until(d -> prdpg.isProductAddedToCart());
            Assert.assertTrue(added, "Product was not added to cart as expected.");

            // Additional asserts can be made, e.g., cart count increased, toast message text, etc.
            logger.info("Product successfully added to cart.");

        } catch (Exception e) {
            logger.error("Error in TC02", e);
            // Re-throw to make sure test is marked failed
            throw e;
        } finally {
            // If BaseTest doesn't handle cleanup, consider:
            // WebdriverUtil.quitDriver();
            // But don't duplicate teardown if BaseTest already does it.
            logger.info("Completed Cart Test Case 02");
        }
    }
}

