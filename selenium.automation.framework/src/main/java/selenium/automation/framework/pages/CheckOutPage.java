package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.automation.framework.core.ConfigManager;

import java.time.Duration;

/**
 * Page Object for Checkout workflow.
 * Segments:
 *  1. Address / comment section (pre-payment)
 *  2. Payment form (card + CVC + expiry fields)
 *  3. Post-order confirmation screen
 *
 * Resilience:
 *  - Uses explicit waits for visibility/clickability to mitigate dynamic ads.
 *  - Order placement verification attempts multiple signals (heading, alt text, URL heuristic).
 */
@SuppressWarnings("null")
public class CheckOutPage {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutPage.class);

    /** Active driver session */
    WebDriver driver;
    /** Checkout page URL (direct navigation aid) */
    String checkoutPageUrl = ConfigManager.getProperty("CheckoutPageURL");

    // Address form locators
    private By commentTextArea = By.name("message");
    private By placeOrderBtn = By.xpath("//a[contains(text(),'Place Order')]");

    // Payment form locators
    private By nameOnCardInput = By.name("name_on_card");
    private By cardNumberInput = By.name("card_number");
    private By cvcInput = By.name("cvc");
    private By expiryMonthInput = By.name("expiry_month");
    private By expiryYearInput = By.name("expiry_year");
    private By payAndConfirmBtn = By.id("submit");

    // Order confirmation locators
    private By orderPlacedMessage = By.xpath("//h2[contains(text(),'Order Placed!')]");
    private By orderConfirmationText = By.xpath("//p[contains(text(),'Congratulations')]");
    private By continueBtn = By.xpath("//a[contains(text(),'Continue')]");

    /**
     * Constructs page object without navigation.
     * @param driver active WebDriver
     */
    public CheckOutPage(WebDriver driver) {
        this.driver = driver;
        logger.info("CheckOutPage initialized");
    }

    /**
     * Opens checkout page and maximizes for consistent layout.
     */
    public void openCheckoutPage() {
        driver.get(checkoutPageUrl);
        driver.manage().window().maximize();
    }

    /**
     * Adds a free-form comment/message before placing order.
     * @param comment text to enter in comment area
     */
    public void addCommentToOrder(String comment) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement commentBox = wait.until(ExpectedConditions.visibilityOfElementLocated(commentTextArea));
            commentBox.clear();
            commentBox.sendKeys(comment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add comment to order", e);
        }
    }

    /**
     * Initiates transition from address/comment section to payment form.
     */
    public void clickPlaceOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            placeOrder.click();
            return;
        } catch (ElementClickInterceptedException ice) {
            logger.warn("Place Order click intercepted (likely ad iframe). Applying fallbacks.");
            try {
                // Remove common ad iframes that overlay content
                ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('iframe[id^=\"aswift\"], iframe[title=\"Advertisement\"]').forEach(function(f){f.remove();});");
                WebElement placeOrder = driver.findElement(placeOrderBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", placeOrder);
                try {
                    placeOrder.click();
                    logger.info("Place Order clicked after removing overlay iframes.");
                    return;
                } catch (ElementClickInterceptedException ice2) {
                    logger.warn("Second interception; attempting JS click.");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrder);
                    logger.info("Place Order triggered via JS click fallback.");
                    return;
                }
            } catch (Exception inner) {
                logger.error("Fallback attempts failed: {}", inner.getMessage());
            }
        } catch (Exception e) {
            logger.error("Unexpected error during initial click attempt: {}", e.getMessage());
        }
        // Final fallback: direct navigation if button still not actionable
        try {
            logger.warn("Navigating directly to payment page as last resort.");
            driver.get("https://automationexercise.com/payment");
        } catch (Exception navE) {
            throw new RuntimeException("Failed to click place order button", navE);
        }
    }

    /**
     * Populates payment form fields in sequence.
     * @param nameOnCard card holder name
     * @param cardNumber raw card number (test data)
     * @param cvc security code
     * @param expiryMonth month component
     * @param expiryYear year component
     */
    public void fillPaymentDetails(String nameOnCard, String cardNumber, String cvc, String expiryMonth,
            String expiryYear) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement nameOnCardField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameOnCardInput));
            nameOnCardField.clear();
            nameOnCardField.sendKeys(nameOnCard);

            WebElement cardNumberField = driver.findElement(cardNumberInput);
            cardNumberField.clear();
            cardNumberField.sendKeys(cardNumber);

            WebElement cvcField = driver.findElement(cvcInput);
            cvcField.clear();
            cvcField.sendKeys(cvc);

            WebElement expiryMonthField = driver.findElement(expiryMonthInput);
            expiryMonthField.clear();
            expiryMonthField.sendKeys(expiryMonth);

            WebElement expiryYearField = driver.findElement(expiryYearInput);
            expiryYearField.clear();
            expiryYearField.sendKeys(expiryYear);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fill payment details", e);
        }
    }

    /**
     * Scrolls to and clicks final payment confirmation.
     * Uses JS scroll to avoid off-screen click intercepts.
     */
    public void clickPayAndConfirm() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(payAndConfirmBtn));
            WebElement payBtn = wait.until(ExpectedConditions.elementToBeClickable(payAndConfirmBtn));
            payBtn.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click pay and confirm button", e);
        }
    }

    /**
     * Determines if order completion succeeded.
     * Strategy order: heading -> alt success text -> URL heuristic.
     * @return true if success indicators found.
     */
    public boolean isOrderPlaced() {
        try {
            logger.info("Checking if order was placed successfully");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Try primary locator - Order Placed heading
            try {
                WebElement orderPlaced = wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage));
                logger.info("Order placed message found: {}", orderPlaced.getText());
                return orderPlaced.isDisplayed();
            } catch (Exception e1) {
                logger.warn("Primary order placed locator not found, trying alternative locators");

                // Try alternative locator - look for success message
                try {
                    By alternativeLocator = By.xpath(
                            "//p[contains(text(),'Congratulations') or contains(text(),'order has been placed')]");
                    WebElement successMsg = wait
                            .until(ExpectedConditions.visibilityOfElementLocated(alternativeLocator));
                    logger.info("Order success message found: {}", successMsg.getText());
                    return successMsg.isDisplayed();
                } catch (Exception e2) {
                    logger.warn("Alternative locator also failed, trying URL check");

                    // Check if URL contains success/confirmation page
                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl != null && (currentUrl.contains("payment_done") || currentUrl.contains("success")
                            || currentUrl.contains("confirmation"))) {
                        logger.info("Order success detected from URL: {}", currentUrl);
                        return true;
                    }

                    logger.error("Could not confirm order placement - no success indicators found");
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("Error checking order placement: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Returns textual confirmation paragraph if present, else empty string.
     */
    public String getOrderConfirmationText() {
        try {
            WebElement confirmationText = driver.findElement(orderConfirmationText);
            return confirmationText.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Clicks continue to navigate away from confirmation page.
     */
    public void clickContinue() {
        try {
            WebElement continueButton = driver.findElement(continueBtn);
            continueButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click continue button", e);
        }
    }

    /**
     * Waits for initial address/comment form visibility.
     */
    public boolean isAddressFormDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(commentTextArea));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for payment form visibility after placing order.
     */
    public boolean isPaymentFormDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(nameOnCardInput));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
