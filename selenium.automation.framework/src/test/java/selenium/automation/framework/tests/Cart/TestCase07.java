package selenium.automation.framework.tests.Cart;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase07 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase07.class);

    @Test
    public void TC07() throws InterruptedException {
        logger.info("Executing Cart Test Case 07 - Proceed to Checkout");
        createTest("CART-07");

        WebDriver driver = WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);

        try {
            // Navigate to products and add item to cart
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();

            // Wait for cart page to load
            Thread.sleep(1000);

            // Verify item is in cart
            boolean itemInCart = cartPage.isItemsInCart(1);
            Assert.assertTrue(itemInCart, "Item should be in cart before checkout");

            // Proceed to checkout
            cartPage.proceedToCheckout();

            // Wait for checkout page to load
            Thread.sleep(2000);

            // Verify checkout page is displayed
            boolean userLoggedIn = cartPage.isUserLoggedIn();
            if (userLoggedIn) {
                boolean checkoutDisplayed = checkoutPage.isAddressFormDisplayed();
                Assert.assertTrue(checkoutDisplayed, "Checkout page should be displayed");
                getTest().pass("✓ Successfully proceeded to checkout");
                logger.info("✓ Checkout navigation successful");
            } else {
                boolean checkoutModalDisplayed = cartPage.isCheckoutModalDisplayed();
                Assert.assertTrue(checkoutModalDisplayed, "Checkout modal should be displayed");
                getTest().pass("✓ Checkout modal displayed for guest user");
                logger.info("✓ Checkout modal displayed successfully for guest user");
            }

        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
