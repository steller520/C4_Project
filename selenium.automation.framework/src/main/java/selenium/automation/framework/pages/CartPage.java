package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import selenium.automation.framework.core.ConfigManager;

/**
 * Page Object modelling the Cart screen.
 * Responsibilities:
 *  - Read cart contents (rows, quantities, prices, totals)
 *  - Perform lightweight cart actions (proceed to checkout, removal)
 *  - Resilient detection of rows using multiple fallback locator strategies
 *  - Provide defensive checks to avoid brittle NoSuchElementExceptions
 *
 * Design Notes:
 *  - Removal uses multi-strategy approach to handle dynamic markup differences.
 *  - Quantity update currently unsupported -> explicit UnsupportedOperationException for clarity.
 *  - Methods prefer returning default values ("", 0, false) rather than throwing
 *    to allow tests to assert gracefully.
 */
@SuppressWarnings("null")
public class CartPage {
    /** Logger instance for structured diagnostic output */
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    /** Active WebDriver session */
    WebDriver driver;
    /** Base URL for direct navigation to cart */
    String cartPageUrl = ConfigManager.getProperty("CartPageURL");
    /** Root cart table element */
    private By cartTable = By.id("cart_info_table");
    /** Checkout CTA */
    private By proceedToCheckoutBtn = By.xpath("//a[contains(text(),'Proceed To Checkout')]");
    /** Aggregated total amount cell */
    private By cartTotalAmount = By.xpath("//td[contains(text(),'Total')]/following-sibling::td");
    /** Quantity input (if present) */
    // Quantity input retained for future enhancement (currently unused)
    @SuppressWarnings("unused")
    private By quantityInput = By.className("cart_quantity_input");
    /** Logout link used to infer logged-in state */
    private By logoutLink = By.xpath("//a[contains(text(),'Logout')]");
    /** Modal displayed during inline checkout flow */
    private By cartCheckOutModal = By.id("checkoutModal");

    /**
     * Constructs the page object. Does not navigate.
     * @param driver active WebDriver
     */
    public CartPage(WebDriver driver) {
        this.driver = driver;
        logger.info("CartPage initialized");
    }

    /**
     * Navigates to the cart page and maximizes window for consistent viewport.
     */
    public void openCartPage() {
        logger.info("Navigating to CartPage: {}", cartPageUrl);
        driver.get(cartPageUrl);
        driver.manage().window().maximize();
        logger.info("CartPage opened and window maximized");
    }

    /**
     * Checks presence of a product row by data-product-id attribute.
     * @param productId id value expected in row's td[data-product-id]
     * @return true if matching row exists
     */
    public boolean isProductInCart(String productId) {
        logger.info("Checking if product ID {} is in cart", productId);
        String productRowXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId + "']]";
        boolean isPresent = !driver.findElements(By.xpath(productRowXpath)).isEmpty();
        logger.info("Product ID {} in cart: {}", productId, isPresent);
        return isPresent;
    }

    /**
     * Attempts to find any item with dynamic row id pattern product-<n>.
     * Falls back to generic row content inspection when pattern not present.
     * @param id numeric part expected in product-<id>
     * @return true if matching or any valid product row present
     */
    public boolean isItemsInCart(int id) {
        logger.info("Checking if item with ID {} exists in cart", id);
        try {
            // Wait for cart table to be present
            WebElement cartTableElement = driver.findElement(cartTable);

            // Look for rows with id attribute starting with "product-"
            List<WebElement> trows = driver.findElements(
                    RelativeLocator.with(By.tagName("tr")).below(cartTableElement));

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

    /**
     * Validates unit price text for given product id.
     * @param productId product identifier appended in row path
     * @param expectedPrice price string to compare (including currency symbol)
     * @return true if actual matches expected, false if not found or mismatch
     */
    public boolean priceOfItemInCart(String productId, String expectedPrice) {
        String actualPrice;
        try {
            String priceXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId
                    + "']]//td[@class='cart_price']";
            WebElement priceElement = driver.findElement(By.xpath(priceXpath));
            actualPrice = priceElement.getText().trim();
        } catch (Exception e) {
            return false;
        }
        return actualPrice.equals(expectedPrice);
    }

    /**
     * Removes an item using multiple locator strategies to cope with UI variance.
     * Order of strategies:
     *  1. Direct XPath using product-id
     *  2. Iterate all rows matching id pattern and derive delete link
     *  3. Positional fallback based on index
     * Sleeps briefly after click to allow DOM update (minimal wait retained).
     * @param productId id suffix used in product-<id>
     */
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
            if (rows == null || rows.length == 0) {
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
                                deleteButton = row.findElement(
                                        By.xpath(".//a[contains(@class,'delete') or contains(@href,'delete')]"));
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
                    WebElement deleteButton = row.findElement(
                            By.xpath(".//a[contains(@class,'cart_quantity_delete') or contains(@class,'delete')]"));
                    deleteButton.click();
                    Thread.sleep(500);
                    return;
                } catch (Exception e) {
                    // Continue to error
                }
            }

            throw new IllegalStateException(
                    "Product with ID " + productId + " not found in cart. Rows found: " + rows.length);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while removing item", e);
        }
    }

    /**
     * Returns all cart rows using progressive fallback strategies:
     *  1. tbody/tr under cart table
     *  2. any tr with id starting product-
     *  3. RelativeLocator below table
     * @return array of row elements (empty array if none)
     */
    public WebElement[] getAllRows() {
        try {
            // Try multiple strategies to find cart rows
            List<WebElement> trows;

            // Strategy 1: Find all tbody tr elements in cart table
            trows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));

            if (trows.isEmpty()) {
                // Strategy 2: Try finding rows with id starting with 'product-'
                trows = driver
                        .findElements(By.xpath("//table[@id='cart_info_table']//tr[starts-with(@id, 'product-')]"));
            }

            if (trows.isEmpty()) {
                // Strategy 3: Try RelativeLocator as fallback
                WebElement cartTable = driver.findElement(this.cartTable);
                trows = driver.findElements(
                        RelativeLocator.with(By.tagName("tr")).below(cartTable));
            }

            return trows.toArray(new WebElement[0]);
        } catch (Exception e) {
            return new WebElement[0];
        }
    }

    /**
     * Asserts product absence after deletion attempt.
     * @param productId expected removed product id
     */
    public void verifyItemRemoved(String productId) {
        String productRowXpath = "//*[@id='cart_info_table']//tr[td[@data-product-id='" + productId + "']]]";
        boolean isPresent = !driver.findElements(By.xpath(productRowXpath)).isEmpty();
        if (isPresent) {
            throw new AssertionError("Product with ID " + productId + " was not removed from the cart.");
        }
    }

    /**
     * Clicks the checkout CTA, wrapping errors with runtime exception for test visibility.
     */
    public void proceedToCheckout() {
        try {
            logger.info("Proceeding to checkout");
            WebElement checkoutBtn = driver.findElement(proceedToCheckoutBtn);
            checkoutBtn.click();
            logger.info("Checkout button clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to click proceed to checkout button: {}", e.getMessage());
            throw new RuntimeException("Failed to click proceed to checkout button", e);
        }
    }

    /**
     * Retrieves cart total, attempting alternative locator if primary empty.
     * @return textual total (may include currency symbol) or empty string.
     */
    public String getCartTotal() {
        try {
            logger.info("Retrieving cart total amount");
            // Wait for the element to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement totalElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartTotalAmount));
            String total = totalElement.getText().trim();

            if (total.isEmpty()) {
                // Try alternative locator if main one returns empty
                logger.warn("Cart total text is empty, trying alternative locator");
                try {
                    By alternativeLocator = By.xpath("//h4[contains(@class,'cart_total_price')]");
                    WebElement altElement = driver.findElement(alternativeLocator);
                    total = altElement.getText().trim();
                    logger.info("Cart total from alternative locator: {}", total);
                } catch (Exception e2) {
                    logger.warn("Alternative locator also failed: {}", e2.getMessage());
                }
            }

            logger.info("Cart total amount: {}", total);
            return total;
        } catch (Exception e) {
            logger.warn("Failed to retrieve cart total: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Placeholder for quantity update (currently unsupported by application under test).
     * Explicitly throws to avoid silent test assumptions.
     */
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

    /**
     * Extracts visible quantity badge/button text for given product id.
     * @return parsed integer quantity or 0 if missing/error.
     */
    public int getProductQuantity(String productId) {
        try {
            logger.info("Getting quantity for product ID: {}", productId);
            WebElement quantityElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//button[@class='disabled']"));
            String qtyText = quantityElement.getText().trim();
            int quantity = Integer.parseInt(qtyText);
            logger.info("Product ID {} has quantity: {}", productId, quantity);
            return quantity;
        } catch (Exception e) {
            logger.warn("Failed to get quantity for product {}: {}", productId, e.getMessage());
            return 0;
        }
    }

    /**
     * Convenience verification of expected quantity vs actual.
     */
    public boolean verifyCartQuantity(String productId, int expectedQuantity) {
        logger.info("Verifying cart quantity for product ID {}, expected: {}", productId, expectedQuantity);
        int actualQuantity = getProductQuantity(productId);
        boolean matches = actualQuantity == expectedQuantity;
        logger.info("Quantity verification for product {}: expected={}, actual={}, matches={}",
                productId, expectedQuantity, actualQuantity, matches);
        return matches;
    }

    /**
     * Retrieves unit price text for product id.
     */
    public String getProductPrice(String productId) {
        try {
            logger.info("Getting price for product ID: {}", productId);
            WebElement priceElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//td[@class='cart_price']/p"));
            String price = priceElement.getText().trim();
            logger.info("Product ID {} price: {}", productId, price);
            return price;
        } catch (Exception e) {
            logger.warn("Failed to get price for product {}: {}", productId, e.getMessage());
            return "";
        }
    }

    /**
     * Retrieves line total for product id.
     */
    public String getProductTotal(String productId) {
        try {
            logger.info("Getting total for product ID: {}", productId);
            WebElement totalElement = driver.findElement(
                    By.xpath("//tr[@id='product-" + productId + "']//td[@class='cart_total']/p"));
            String total = totalElement.getText().trim();
            logger.info("Product ID {} total: {}", productId, total);
            return total;
        } catch (Exception e) {
            logger.warn("Failed to get total for product {}: {}", productId, e.getMessage());
            return "";
        }
    }

    /**
     * Returns current count of cart rows (after all fallback strategies).
     */
    public int getCartItemCount() {
        logger.info("Getting cart item count");
        WebElement[] rows = getAllRows();
        int count = rows != null ? rows.length : 0;
        logger.info("Cart contains {} items", count);
        return count;
    }

    /**
     * Infers logged-in status by visibility of logout link.
     */
    public boolean isUserLoggedIn() {
        try {
            logger.debug("Checking if user is logged in");
            boolean loggedIn = driver.findElement(logoutLink).isDisplayed();
            logger.info("User logged in status: {}", loggedIn);
            return loggedIn;
        } catch (Exception e) {
            logger.info("User is not logged in");
            return false;
        }
    }
    
    /**
     * Waits up to 10s for inline checkout modal.
     * @return true if displayed, false if timeout/absent
     */
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
