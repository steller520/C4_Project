package selenium.automation.framework.tests.Cart;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

/**
 * CART-04: Remove item from cart after adding.
 * Flow: add product id 2 -> navigate cart -> remove -> verify absence.
 */
public class TestCase04 extends BaseTest {
    @Test
    public void TC04() throws InterruptedException {
        System.out.println("Executing Cart Test Case 04 - Remove Item from Cart");
        createTest("CART-04");
        getTest().info("========== Starting Test Case: CART-04 - Remove Item from Cart ==========");
        getTest().info("WebDriver initialized successfully");
        getTest().info("Test Objective: Verify item can be removed from cart successfully");
        
        WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(WebdriverUtil.getDriver());
        ProductsPage productsPage = new ProductsPage(WebdriverUtil.getDriver());
        
        // Step 1: Open products
        getTest().info("Step 1: Navigating to Products Page");
        productsPage.openProductsPage();
        getTest().info("Products page opened");
        
        // Step 2: Add product #2
        getTest().info("Step 2: Adding Product to Cart (Product ID: 2)");
        productsPage.addFirstProductToCart(2);
        getTest().info("Product #2 added to cart");
        
        // Step 3: View cart
        getTest().info("Step 3: Navigating to Cart Page");
        productsPage.clickViewCartLink();
        getTest().info("Cart page opened");
        
        WebDriverWait wait = new WebDriverWait(WebdriverUtil.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("view_cart"));
        
        // Step 4: Pre-removal presence check
        getTest().info("Step 4: Verifying Product in Cart Before Removal");
        boolean itemInCartBefore = cartPage.isItemsInCart(2);
        Assert.assertTrue(itemInCartBefore, "Item should be in cart before removal");
        getTest().info("Product ID 2 found in cart: " + itemInCartBefore);
        getTest().pass("✓ Product verified in cart before removal");
        
        // Step 5: Remove product
        getTest().info("Step 5: Removing Product from Cart");
        cartPage.removeItemFromCart("2");
        getTest().info("Remove item action triggered for Product ID: 2");
        
        // Wait for cart to update after removal
        try {
            Thread.sleep(500); // Brief wait for DOM update
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Step 6: Post-removal absence check
        getTest().info("Step 6: Verifying Product Removed from Cart");
        boolean itemInCartAfter = cartPage.isItemsInCart(2);
        Assert.assertFalse(itemInCartAfter, "Item should be removed from cart");
        getTest().info("Product ID 2 in cart after removal: " + itemInCartAfter);
        
        if(!itemInCartAfter) {
            getTest().pass("✓ Item successfully removed from cart");
            getTest().info("Cart is updated correctly after removal");
        } else {
            getTest().fail("✗ Item still exists in cart after removal");
        }
        
        System.out.println("✓ Item successfully removed from cart");
        getTest().info("========== Test Case CART-04 Completed ==========");
    }
    
}
