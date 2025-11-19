package selenium.automation.framework.tests.Cart;

import org.testng.Assert;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.CartPage;
import selenium.automation.framework.pages.ProductsPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase04 extends BaseTest {
    // Test removing item from cart
    @Test
    public void TC04() throws InterruptedException {
        System.out.println("Executing Cart Test Case 04 - Remove Item from Cart");
        createTest("CART-04");
        
        WebdriverUtil.getDriver();
        CartPage cartPage = new CartPage(WebdriverUtil.getDriver());
        ProductsPage productsPage = new ProductsPage(WebdriverUtil.getDriver());
        
        // Navigate to products and add item to cart
        productsPage.openProductsPage();
        productsPage.addFirstProductToCart(2);
        productsPage.clickViewCartLink();
        
        // Wait for cart page to load
        Thread.sleep(1000);
        
        // Verify item is in cart before removal
        boolean itemInCartBefore = cartPage.isItemsInCart(2);
        Assert.assertTrue(itemInCartBefore, "Item should be in cart before removal");
        
        // Remove the item
        cartPage.removeItemFromCart("2");
        
        // Wait for removal to complete
        Thread.sleep(1000);
        
        // Verify item is removed from cart
        boolean itemInCartAfter = cartPage.isItemsInCart(2);
        Assert.assertFalse(itemInCartAfter, "Item should be removed from cart");
        
        System.out.println("✓ Item successfully removed from cart");
    }
    
}
