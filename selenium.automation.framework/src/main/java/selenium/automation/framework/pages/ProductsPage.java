package selenium.automation.framework.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.automation.framework.core.ConfigManager;

/**
 * Page Object representing the Products listing/catalog screen.
 * Provides minimal interactions required by current test scope:
 *  - Navigate to products page
 *  - Add a product to cart (by dynamic data-product-id attribute)
 *  - Validate add-to-cart confirmation modal
 *  - Navigate to cart via modal link
 *
 * Implementation choices:
 *  - Dynamic locator generation for Add to Cart stored in instance field to reuse.
 *  - Scroll into view before clicking to reduce interception by floating banners.
 *  - FluentWait used for modal reliability with custom polling + message.
 */
@SuppressWarnings("null")
public class ProductsPage {
    /** Logger for structured events */
    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);
    /** Driver session */
    WebDriver  driver;
    
    /** Constructor does not navigate */
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        logger.info("ProductsPage initialized");
    }

    /** Base URL for direct navigation */
    private String ProductsPageUrl = ConfigManager.getProperty("ProductsPageURL");

    // Locators
    /** Dynamic Add To Cart button (assigned when product id resolved) */
    private By addToCartButton;
    /** Confirmation region inside modal after successful add */
    private By productAddedConfirmation = By.xpath("/html/body/section[2]/div/div/div[2]/div/div[1]/div/div");
    /** Link to view cart within modal */
    private By linkViewCart = By.xpath("//div[@id=\"cartModal\"]/div/div/div[2]/p[2]/a[1]");
    /** Cart modal container */
    private By cartModal = By.id("cartModal");

    /** Application currently exposes 43 products (used for guard loop) */
    int productcount=43;

    /**
     * Builds and stores locator for given product id.
     * @param id product numeric id (1..productcount)
     * @return By pointing to add-to-cart element
     */
    public By getAddToCartButton(int id) {
        for(int i=1;i<=productcount;i++) {
            if(id==i){
                addToCartButton = By.xpath("//*[@data-product-id='"+i+"']");
                break;
            }
        }
        return addToCartButton;
    } 

    /**
     * Navigates to products page and maximizes window.
     */
    public void openProductsPage() {
        logger.info("Navigating to ProductsPage: {}", ProductsPageUrl);
        driver.get(ProductsPageUrl);
        driver.manage().window().maximize();
        logger.info("ProductsPage opened and window maximized");
    }

    /**
     * Adds requested product to cart. Defaults id=1 if zero passed.
     * Scrolls element into center before click, waits for clickability.
     * @param id product id (1-based)
     * @return false (reserved for future true/false success flag)
     */
    public boolean addFirstProductToCart(int id) {
        if(id == 0) {
            id = 1;
        }
        logger.info("Adding product with ID {} to cart", id);
        getAddToCartButton(id);
        WebElement addToCartButton = driver.findElement(this.addToCartButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", addToCartButton);
        logger.debug("Scrolled to product ID: {}", id);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        logger.info("Product ID {} added to cart successfully", id);
        return false;
    }

    /**
     * Confirms product added modal is visible.
     * @return true if confirmation element displayed
     */
    public boolean isProductAddedToCart() {
        logger.info("Verifying product added to cart confirmation");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productAddedConfirmation));
        boolean isDisplayed = driver.findElement(productAddedConfirmation).isDisplayed();
        logger.info("Product added confirmation displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Clicks View Cart link inside modal with fluent waiting to handle animation.
     */
    public void clickViewCartLink() {
        logger.info("Clicking 'View Cart' link");
        FluentWait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(Exception.class)
                .withMessage("Timed out waiting for View Cart link to be clickable");
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartModal));
        wait.until(ExpectedConditions.elementToBeClickable(linkViewCart));
        driver.findElement(linkViewCart).click();
        logger.info("View Cart link clicked successfully");
    }
}
