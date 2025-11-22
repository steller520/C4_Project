package selenium.automation.framework.tests.Checkout;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.CheckOutPage;
import selenium.automation.framework.pages.LoginPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CHECKOUT-05: Multi-tab product addition scenario verifying cart synchronization and checkout page persistence.
 * Flow: Login -> Add product in Tab1 -> New Tab -> Add second product -> Verify both -> Return Tab1 -> Refresh -> Checkout -> Close Tab2.
 */
public class TestCase05 extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestCase05.class);

    @Parameters({"email", "password"})
    @Test
    public void TC05(@Optional("john.doe+test1@example.com") String email, @Optional("Passw0rd!") String password) {
        logger.info("Executing Checkout Test Case 05 - Tab Switching and Checkout");
        createTest("CHECKOUT-05");
        getTest().info("========== Starting Test Case: CHECKOUT-05 - Tab Switching ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify checkout flow with tab switching between products");
        getTest().info("Test Credentials: Email=" + email + ", Password=****");
        
        WebDriver driver = WebdriverUtil.getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        CartPage cartPage = new CartPage(driver);
        // Instantiate checkout page only when needed later (lazy); removing unused variable warning.
        // CheckOutPage checkoutPage = new CheckOutPage(driver); // not required until after cart verification
        LoginPage loginPage = new LoginPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Store the original window handle
        String originalWindow = driver.getWindowHandle();
        getTest().info("Original window handle stored: " + originalWindow);
        
        try {
            // Step 1: Authenticate user
            getTest().info("Step 1: Performing User Login");
            loginPage.openLoginPage();
            getTest().info("Login page opened");
            loginPage.performLogin(email, password);
            getTest().info("User authenticated successfully");
            
            wait.until(d -> {
                String url = driver.getCurrentUrl();
                return url != null && !url.contains("login");
            });
            
            // Step 2: Open products catalog
            getTest().info("Step 2: Navigating to Products Page");
            productsPage.openProductsPage();
            getTest().info("Products page loaded");
            
            // Step 3: Add first product (current tab)
            getTest().info("Step 3: Opening Product in Current Tab");
            productsPage.addFirstProductToCart(1);
            getTest().info("Product #1 added to cart");
            
            // Step 4: Open cart
            getTest().info("Step 4: Viewing Cart");
            productsPage.clickViewCartLink();
            wait.until(ExpectedConditions.urlContains("view_cart"));
            getTest().info("Cart page opened");
            
            // Step 5: Assert product presence (Tab1)
            getTest().info("Step 5: Verifying Product in Cart (Tab 1)");
            boolean itemInCart = cartPage.isItemsInCart(1);
            Assert.assertTrue(itemInCart, "Product 1 should be in cart");
            getTest().info("Product #1 verified in cart: " + itemInCart);
            getTest().pass("✓ Product verified in first tab");
            
            // Step 6: Launch second tab
            getTest().info("Step 6: Opening Products Page in New Tab");
            // Open products page in a new tab using JavaScript
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open('https://automationexercise.com/products', '_blank');");
            getTest().info("New tab opened");
            
            // Wait for new window/tab to open
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            
            // Get all window handles
            Set<String> allWindows = driver.getWindowHandles();
            getTest().info("Total windows/tabs open: " + allWindows.size());
            
            // Switch to the new tab
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    getTest().info("Switched to new tab: " + windowHandle);
                    break;
                }
            }
            
            // Step 7: Add second product (Tab2)
            getTest().info("Step 7: Adding Product in New Tab");
            // Wait for products page to load in new tab
            wait.until(ExpectedConditions.urlContains("products"));
            getTest().info("Products page loaded in new tab");
            
            ProductsPage productsPageTab2 = new ProductsPage(driver);
            productsPageTab2.addFirstProductToCart(2);
            getTest().info("Product #2 added to cart in new tab");
            
            productsPageTab2.clickViewCartLink();
            wait.until(ExpectedConditions.urlContains("view_cart"));
            getTest().info("Cart page opened in new tab");
            
            // Step 8: Verify both products present (Tab2)
            getTest().info("Step 8: Verifying Both Products in Cart (Tab 2)");
            CartPage cartPageTab2 = new CartPage(driver);
            boolean item1InCart = cartPageTab2.isItemsInCart(1);
            boolean item2InCart = cartPageTab2.isItemsInCart(2);
            getTest().info("Product #1 in cart (Tab 2): " + item1InCart);
            getTest().info("Product #2 in cart (Tab 2): " + item2InCart);
            Assert.assertTrue(item1InCart, "Product 1 should be in cart");
            Assert.assertTrue(item2InCart, "Product 2 should be in cart");
            getTest().pass("✓ Both products verified in second tab");
            
            // Step 9: Switch back to original tab
            getTest().info("Step 9: Switching Back to Original Tab");
            driver.switchTo().window(originalWindow);
            getTest().info("Switched back to original tab");
            
            // Verify we're on cart page
            wait.until(d -> {
                String url = driver.getCurrentUrl();
                return url != null && url.contains("view_cart");
            });
            getTest().info("Original tab URL: " + driver.getCurrentUrl());
            
            // Step 10: Refresh to sync cart state
            getTest().info("Step 10: Refreshing Cart in Original Tab");
            driver.navigate().refresh();
            wait.until(ExpectedConditions.urlContains("view_cart"));
            getTest().info("Cart page refreshed");
            
            CartPage cartPageTab1 = new CartPage(driver);
            boolean bothItemsInOriginalTab = cartPageTab1.isItemsInCart(1) && cartPageTab1.isItemsInCart(2);
            getTest().info("Both products in cart (Original Tab after refresh): " + bothItemsInOriginalTab);
            Assert.assertTrue(bothItemsInOriginalTab, "Both products should be in cart after refresh");
            getTest().pass("✓ Cart synchronized across tabs");
            
            // Step 11: Proceed to checkout (Tab1)
            getTest().info("Step 11: Proceeding to Checkout from Original Tab");
            cartPageTab1.proceedToCheckout();
            wait.until(ExpectedConditions.urlContains("checkout"));
            getTest().info("Checkout page loaded");
            
            // Step 12: Validate checkout page visible
            getTest().info("Step 12: Verifying Checkout Page Display");
            CheckOutPage checkoutPageTab1 = new CheckOutPage(driver);
            boolean isCheckoutDisplayed = checkoutPageTab1.isAddressFormDisplayed();
            Assert.assertTrue(isCheckoutDisplayed, "Checkout page should be displayed");
            getTest().info("Checkout form displayed: " + isCheckoutDisplayed);
            getTest().pass("✓ Checkout page verified");
            
            // Step 13: Close second tab to ensure no dependency
            getTest().info("Step 13: Switching to Second Tab and Closing It");
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            // Safe switch: ensure index exists and handle potential null handle value
            if (tabs.size() > 1) {
                String secondHandle = tabs.get(1);
                if (secondHandle != null) {
                    driver.switchTo().window(secondHandle);
                }
            }
            getTest().info("Switched to second tab for closing");
            driver.close();
            getTest().info("Second tab closed");
            
            // Step 14: Return to original tab and ensure checkout still accessible
            getTest().info("Step 14: Switching Back to Original Tab");
            // Return to original tab safely
            if (!tabs.isEmpty()) {
                String firstHandle = tabs.get(0);
                if (firstHandle != null) {
                    driver.switchTo().window(firstHandle);
                }
            }
            getTest().info("Back to original tab");
            
            // Verify we're still on checkout page
            wait.until(d -> {
                String url = driver.getCurrentUrl();
                return url != null && url.contains("checkout");
            });
            getTest().info("Final URL: " + driver.getCurrentUrl());
            
            getTest().pass("✓ Successfully completed tab switching test with checkout");
            getTest().info("Tab switching demonstrated: Open new tab → Add product → Switch between tabs → Close tab");
            logger.info("✓ Tab switching test completed successfully");
            
        } catch (Exception e) {
            logger.error("Error in TC05", e);
            getTest().fail("✗ Test failed with exception: " + e.getMessage());
            throw e;
        } finally {
            logger.info("Completed Checkout Test Case 05");
            getTest().info("========== Test Case CHECKOUT-05 Completed ==========");
        }
    }
}
