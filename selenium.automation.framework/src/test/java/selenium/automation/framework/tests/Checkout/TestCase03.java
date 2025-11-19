package selenium.automation.framework.tests.Checkout;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.model.Log;


import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase03 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase03.class);

    @Parameters({"email", "password"})
    @Test
    public void TC03(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) throws InterruptedException {
        logger.info("Executing Checkout Test Case 03 - Fill Payment Details");
        createTest("CHECKOUT-03");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckOutPage checkoutPage = new CheckOutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        
        try {
            loginPage.openLoginPage();
            loginPage.performLogin(email, password);
            Thread.sleep(2000);
            
            
            // Add product to cart and navigate to checkout
            productsPage.openProductsPage();
            productsPage.addFirstProductToCart(1);
            productsPage.clickViewCartLink();
            Thread.sleep(1000);
            
            cartPage.proceedToCheckout();
            Thread.sleep(2000);
            
            // Add comment and place order
            checkoutPage.addCommentToOrder("Test order comment");
            checkoutPage.clickPlaceOrder();
            Thread.sleep(2000);
            
            // Fill payment details
            String nameOnCard = "John Doe";
            String cardNumber = "4532015112830366";
            String cvc = "123";
            String expiryMonth = "12";
            String expiryYear = "2028";
            
            checkoutPage.fillPaymentDetails(nameOnCard, cardNumber, cvc, expiryMonth, expiryYear);
            logger.info("Filled payment details successfully");
            
            getTest().info("Name on Card: " + nameOnCard);
            getTest().info("Card Number: " + cardNumber);
            getTest().pass("✓ Payment details filled successfully");
            logger.info("✓ Payment form completed");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: " + e.getMessage());
            getTest().fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
