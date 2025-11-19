package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.automation.framework.core.ConfigManager;

import java.time.Duration;

public class CheckOutPage {
    
    WebDriver driver;
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
    
    public CheckOutPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void openCheckoutPage() {
        driver.get(checkoutPageUrl);
        driver.manage().window().maximize();
    }
    
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
    
    public void clickPlaceOrder() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            placeOrder.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click place order button", e);
        }
    }
    
    public void fillPaymentDetails(String nameOnCard, String cardNumber, String cvc, String expiryMonth, String expiryYear) {
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
    
    public boolean isOrderPlaced() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement orderPlaced = wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage));
            return orderPlaced.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getOrderConfirmationText() {
        try {
            WebElement confirmationText = driver.findElement(orderConfirmationText);
            return confirmationText.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    public void clickContinue() {
        try {
            WebElement continueButton = driver.findElement(continueBtn);
            continueButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click continue button", e);
        }
    }
    
    public boolean isAddressFormDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(commentTextArea));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
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

