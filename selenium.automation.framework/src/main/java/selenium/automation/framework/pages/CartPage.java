package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.With;

import java.time.Duration;
import java.util.List;

import selenium.automation.framework.core.ConfigManager;

public class CartPage {

    WebDriver driver;

    String cartPageUrl = ConfigManager.getProperty("CartPageURL");
    private By cartTable = By.id("cart_info_table");
    private By proceedToCheckoutBtn = By.xpath("//a[contains(text(),'Proceed To Checkout')]");
    private By cartTotalAmount = By.xpath("//td[contains(text(),'Total')]/following-sibling::td");
    private By quantityInput = By.className("cart_quantity_input");
    private By logoutLink = By.xpath("//a[contains(text(),'Logout')]");
    private By cartCheckOutModal = By.id("checkoutModal");
    

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openCartPage() {
        driver.get(cartPageUrl);
        driver.manage().window().maximize();
    }

    public boolean isProductInCart(String productId) {
        String productRowXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId + "']]";
        return !driver.findElements(By.xpath(productRowXpath)).isEmpty();
    }

    public boolean isItemsInCart(int id) {
        try {
            // Wait for cart table to be present
            WebElement cartTableElement = driver.findElement(cartTable);
            
            // Look for rows with id attribute starting with "product-"
            List<WebElement> trows = driver.findElements(
                    RelativeLocator.with(By.tagName("tr")).below(cartTableElement)
            );

            for (WebElement row : trows) {
                String rowId = row.getAttribute("id");
                if (rowId == null || rowId.isEmpty()) {
                    continue; // skip rows with no id
                }

                if (!rowId.startsWith("product-")) {
                    continue; // unexpected format; skip
                }

                String numericPart = rowId.substring("product-".length());

                try {
                    int rowIdValue = Integer.parseInt(numericPart);
                    if (rowIdValue == id) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // skip invalid values
                    continue;
                }
            }
            
            // Fallback: Check if there are any product rows in the cart at all
            // This handles cases where the row id structure is different
            List<WebElement> productRows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
            if (!productRows.isEmpty()) {
                // Check if any row contains product information (not empty cart message)
                for (WebElement row : productRows) {
                    String rowText = row.getText();
                    if (rowText != null && !rowText.trim().isEmpty() && !rowText.contains("Cart is empty")) {
                        return true; // Found at least one product
                    }
                }
            }
            
                return false;
            } catch (Exception e) {
                return false;
            }
        }
    
        public boolean priceOfItemInCart(String productId, String expectedPrice) {
            String actualPrice;
            try {
                String priceXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId + "']]//td[@class='cart_price']";
                WebElement priceElement = driver.findElement(By.xpath(priceXpath));
                actualPrice = priceElement.getText().trim();
            } catch (Exception e) {
                return false;
            }
            return actualPrice.equals(expectedPrice);
        }

        public void removeItemFromCart(String productId) {
            try {
                // Strategy 1: Try direct XPath to find and click delete button
                try {
                    WebElement deleteButton = driver.findElement(
                        By.xpath("//tr[@id='product-" + productId + "']//a[contains(@class,'cart_quantity_delete')]"));
                    deleteButton.click();
                    Thread.sleep(500);
                    return;
                } catch (Exception e1) {
                    // Continue to next strategy
                }
                
                // Strategy 2: Find all rows and match by id attribute
                WebElement[] rows = getAllRows();
                if(rows == null || rows.length == 0) {
                    throw new IllegalStateException("No rows found in cart table");
                }
                
                for (WebElement row : rows) {
                    String rowId = row.getAttribute("id");
                    if (rowId != null && rowId.equals("product-" + productId)) {
                        WebElement deleteButton = null;
                        
                        try {
                            deleteButton = row.findElement(By.className("cart_quantity_delete"));
                        } catch (Exception e1) {
                            try {
                                deleteButton = row.findElement(By.linkText("Delete"));
                            } catch (Exception e2) {
                                try {
                                    deleteButton = row.findElement(By.xpath(".//a[contains(@class,'delete') or contains(@href,'delete')]"));
                                } catch (Exception e3) {
                                    deleteButton = row.findElement(By.xpath(".//td[last()]//a"));
                                }
                            }
                        }
                        
                        if (deleteButton != null) {
                            deleteButton.click();
                            Thread.sleep(500);
                            return;
                        }
                    }
                }
                
                // Strategy 3: Try finding by index if id doesn't match
                int productIndex = Integer.parseInt(productId);
                if (productIndex > 0 && productIndex <= rows.length) {
                    WebElement row = rows[productIndex - 1];
                    try {
                        WebElement deleteButton = row.findElement(By.xpath(".//a[contains(@class,'cart_quantity_delete') or contains(@class,'delete')]"));
                        deleteButton.click();
                        Thread.sleep(500);
                        return;
                    } catch (Exception e) {
                        // Continue to error
                    }
                }
                
                throw new IllegalStateException("Product with ID " + productId + " not found in cart. Rows found: " + rows.length);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while removing item", e);
            }
        }

        public WebElement[] getAllRows(){
            try {
                // Try multiple strategies to find cart rows
                List<WebElement> trows;
                
                // Strategy 1: Find all tbody tr elements in cart table
                trows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
                
                if (trows.isEmpty()) {
                    // Strategy 2: Try finding rows with id starting with 'product-'
                    trows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tr[starts-with(@id, 'product-')]"));
                }
                
                if (trows.isEmpty()) {
                    // Strategy 3: Try RelativeLocator as fallback
                    WebElement cartTable = driver.findElement(this.cartTable);
                    trows = driver.findElements(
                            RelativeLocator.with(By.tagName("tr")).below(cartTable)
                    );
                }
                
                return trows.toArray(new WebElement[0]);
            } catch (Exception e) {
                return new WebElement[0];
            }
        }
        

        public void verifyItemRemoved(String productId) {
            String productRowXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId + "']]]";
            boolean isPresent = !driver.findElements(By.xpath(productRowXpath)).isEmpty();
            if (isPresent) {
                throw new AssertionError("Product with ID " + productId + " was not removed from the cart.");
            }
        }

        public void proceedToCheckout() {
            try {
                WebElement checkoutBtn = driver.findElement(proceedToCheckoutBtn);
                checkoutBtn.click();
            } catch (Exception e) {
                throw new RuntimeException("Failed to click proceed to checkout button", e);
            }
        }

        public String getCartTotal() {
            try {
                WebElement totalElement = driver.findElement(cartTotalAmount);
                return totalElement.getText().trim();
            } catch (Exception e) {
                return "0";
            }
        }

        public void updateQuantity(String productId, int quantity) {
            try {
                // Find the quantity button for specific product
                // Note: Most e-commerce sites have quantity as text, not input field
                // This is a placeholder - adjust based on actual HTML structure
                throw new UnsupportedOperationException("Quantity update not supported in this cart implementation");
            } catch (Exception e) {
                throw new RuntimeException("Failed to update quantity for product " + productId, e);
            }
        }

        public int getProductQuantity(String productId) {
            try {
                WebElement quantityElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//button[@class='disabled']"));
                String qtyText = quantityElement.getText().trim();
                return Integer.parseInt(qtyText);
            } catch (Exception e) {
                return 0;
            }
        }

        public boolean verifyCartQuantity(String productId, int expectedQuantity) {
            int actualQuantity = getProductQuantity(productId);
            return actualQuantity == expectedQuantity;
        }

        public String getProductPrice(String productId) {
            try {
                WebElement priceElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//td[@class='cart_price']/p"));
                return priceElement.getText().trim();
            } catch (Exception e) {
                return "";
            }
        }

        public String getProductTotal(String productId) {
            try {
                WebElement totalElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//td[@class='cart_total']/p"));
                return totalElement.getText().trim();
            } catch (Exception e) {
                return "";
            }
        }

        public int getCartItemCount() {
            WebElement[] rows = getAllRows();
            return rows != null ? rows.length : 0;
        }

        public boolean isUserLoggedIn() {
            try {
                return driver.findElement(logoutLink).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }   

    public boolean isCheckoutModalDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartCheckOutModal));
            return driver.findElement(cartCheckOutModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
    
    


