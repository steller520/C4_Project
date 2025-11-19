package selenium.automation.framework.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.automation.framework.core.ConfigManager;

public class ProductsPage {
    // WebDriver instance to interact with the web page
    WebDriver  driver;
    // Constructor to initialize the WebDriver
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    // URL of the products page
    private String ProductsPageUrl = ConfigManager.getProperty("ProductsPageURL");


    private By addToCartButton;
    private By productAddedConfirmation = By.xpath("/html/body/section[2]/div/div/div[2]/div/div[1]/div/div");
    private By linkViewCart = By.xpath("//div[@id=\"cartModal\"]/div/div/div[2]/p[2]/a[1]");
    private By cartModal = By.id("cartModal");

    int productcount=43;
    public By getAddToCartButton(int id) {
        for(int i=1;i<=productcount;i++) {
            if(id==i){
                addToCartButton = By.xpath("//*[@data-product-id='"+i+"']");
                break;

            }
        }  
    return addToCartButton;
    } 


    public void openProductsPage() {
        driver.get(ProductsPageUrl);
        driver.manage().window().maximize();
    }

    public boolean addFirstProductToCart(int id) {
        if(id == 0) {
            id = 1;
        }
        getAddToCartButton(id);
        WebElement addToCartButton = driver.findElement(this.addToCartButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", addToCartButton);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        return false;
    }


    public boolean isProductAddedToCart() {
        
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productAddedConfirmation));
        return driver.findElement(productAddedConfirmation).isDisplayed();

    }

    public void clickViewCartLink() {
        FluentWait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(Exception.class)
                .withMessage("Timed out waiting for View Cart link to be clickable");
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartModal));
        wait.until(ExpectedConditions.elementToBeClickable(linkViewCart));
        driver.findElement(linkViewCart).click();
    }

    
}
