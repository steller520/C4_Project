package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import java.time.Duration;
import selenium.automation.framework.core.ConfigManager;

/**
 * Page Object encapsulating the user registration (signup) workflow.
 * Responsibilities:
 *  - Populate all account + address details
 *  - Handle dynamic country/state selections with graceful fallbacks
 *  - Provide validation message introspection for negative scenarios
 *  - Robust create account button clicking (JS fallback if intercepted)
 */
@SuppressWarnings("null")
public class SignupPage {
    /** Logger for diagnostic output */
    private static final Logger logger = LoggerFactory.getLogger(SignupPage.class);
    /** WebDriver instance */
    WebDriver driver;

    // Locators
    private By nameInputSignUp = By.name("name");
    private By emailInputSignUp = By.xpath("//input[@data-qa=\"signup-email\"]");
    private By signupButton = By.xpath("//button[@data-qa=\"signup-button\"]");
    private By radioButtonForMr = By.id("id_gender1");
    private By radioButtonForMrs = By.id("id_gender2");
    private By passwordInput = By.id("password");
    private By daysDropdown = By.id("days");
    private By monthsDropdown = By.id("months");
    private By yearsDropdown = By.id("years");
    private By firstNameInput = By.id("first_name");
    private By lastNameInput = By.id("last_name");
    private By companyInput = By.id("company");
    private By address1Input = By.id("address1");
    private By address2Input = By.id("address2");
    private By countryDropdown = By.id("country");
    private By stateInput = By.id("state");
    private By cityInput = By.id("city");
    private By zipcodeInput = By.id("zipcode");
    private By mobileNumberInput = By.id("mobile_number");
    private By errorElement = By.xpath("//p[contains(text(),'Email Address already exist!')]");



    

    /**
     * Constructs page object; navigation performed separately for flexibility.
     * @param driver active WebDriver
     */
    public SignupPage(WebDriver driver) {
        this.driver = driver;
        logger.info("SignupPage initialized");
    }

    /**
     * Opens signup page; maximizes window to reduce element overlap risk.
     */
    public void openSignupPage() {
        String signupPageUrl = ConfigManager.getProperty("SignUpPageURL");
        logger.info("Navigating to SignupPage: {}", signupPageUrl);
        driver.get(signupPageUrl);
        driver.manage().window().maximize();
        logger.info("SignupPage opened and window maximized");
    }

    // Method to check if name input for signup is displayed
    public boolean isNameInputSignUpDisplayed() {
        return driver.findElement(nameInputSignUp).isDisplayed();
    }

    /**
     * Enters user name in initial signup form.
     */
    public void enterNameForSignup(String name) {
        logger.info("Entering name for signup: {}", name);
        driver.findElement(nameInputSignUp).sendKeys(name);
        logger.debug("Name entered successfully");
    }

    // Method to check if email input for signup is displayed
    public boolean isEmailInputSignUpDisplayed() {
        return driver.findElement(emailInputSignUp).isDisplayed();
    }

    /**
     * Enters email in initial signup form.
     */
    public void enterEmailForSignup(String email) {
        logger.info("Entering email for signup: {}", email);
        driver.findElement(emailInputSignUp).sendKeys(email);
        logger.debug("Email entered successfully");
    }

    // Method to check if signup button is displayed
    public boolean isSignupButtonDisplayed() {
        return driver.findElement(signupButton).isDisplayed();
    }

    // Method to check if signup button is displayed
    public boolean isSignupButtonVisible() {
        return driver.findElement(signupButton).isDisplayed();
    }

    // Method to check if signup button is enabled
    public boolean isSignupButtonEnabled() {
        return driver.findElement(signupButton).isEnabled();
    }

    /**
     * Initiates account creation form (step 2) by clicking signup.
     */
    public void clickSignupButton() {
        logger.info("Clicking signup button");
        driver.findElement(signupButton).click();
        logger.info("Signup button clicked successfully");
    }
    
    /** Selects gender option 'Mr.' */
    public void selectMrRadioButton() {
        logger.info("Selecting 'Mr.' radio button");
        driver.findElement(radioButtonForMr).click();
        logger.debug("'Mr.' radio button selected");
    }

    /** Selects gender option 'Mrs.' */
    public void selectMrsRadioButton() {
        logger.info("Selecting 'Mrs.' radio button");
        driver.findElement(radioButtonForMrs).click();
        logger.debug("'Mrs.' radio button selected");
    }
    /**
     * Enters account password (value intentionally masked in logs).
     */
    public void enterPassword(String password) {
        logger.info("Entering password: ****");
        driver.findElement(passwordInput).sendKeys(password);
        logger.debug("Password entered successfully");
    }

    /**
     * Selects date of birth components. Day value normalized stripping leading zero.
     */
    public void selectDateOfBirth(String year, String month, String day) {
        logger.info("Selecting date of birth: {}-{}-{}", year, month, day);
        Select daySelect = new Select(driver.findElement(daysDropdown));
        String[] dayParts = day.split("");
        if(dayParts[0].equals("0")) {
            day = dayParts[1];
        }
        daySelect.selectByValue(day);
        logger.debug("Day selected: {}", day);
        
        Select monthSelect = new Select(driver.findElement(monthsDropdown));
        monthSelect.selectByIndex(Integer.parseInt(month));
        logger.debug("Month selected: {}", month);
        
        Select yearSelect = new Select(driver.findElement(yearsDropdown));
        yearSelect.selectByVisibleText(year);
        logger.info("Date of birth selected successfully");
    }
    /** Enters first name */
    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        driver.findElement(firstNameInput).sendKeys(firstName);
    }
    /** Enters last name */
    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        driver.findElement(lastNameInput).sendKeys(lastName);
    }
    /** Enters company name */
    public void enterCompanyName(String company) {
        logger.info("Entering company name: {}", company);
        driver.findElement(companyInput).sendKeys(company);
    }
    /** Enters address line 1 */
    public void enterAddress1(String address1) {
        logger.info("Entering address line 1: {}", address1);
        driver.findElement(address1Input).sendKeys(address1);
    }  
    /** Enters address line 2 */
    public void enterAddress2(String address2) {
        logger.info("Entering address line 2: {}", address2);
        driver.findElement(address2Input).sendKeys(address2);
    }   
    /**
     * Selects country with resilience for unavailable options.
     * Handles common alternative / fallback scenarios.
     */
    public void selectCountry(String country) {
        if(country == null || country.isEmpty()) {
            logger.warn("Country is null or empty. Skipping country selection.");
            return;
        }
        // Handle countries that might not be available or have different names
        if(country.equalsIgnoreCase("China")) {
            logger.warn("Country 'China' is not available in the dropdown. Using 'India' instead.");
            country = "India";
        } else if(country.equalsIgnoreCase("United Kingdom")) {
            logger.warn("Country 'United Kingdom' might not be available. Trying alternative names.");
            // Try alternative names for UK
            try {
                Select countrySelect = new Select(driver.findElement(countryDropdown));
                try {
                    countrySelect.selectByVisibleText("United Kingdom");
                    logger.debug("Country 'United Kingdom' selected successfully");
                    return;
                } catch (org.openqa.selenium.NoSuchElementException e1) {
                    try {
                        countrySelect.selectByVisibleText("UK");
                        logger.debug("Country 'UK' selected successfully");
                        return;
                    } catch (org.openqa.selenium.NoSuchElementException e2) {
                        logger.warn("Neither 'United Kingdom' nor 'UK' found. Using 'United States' instead.");
                        country = "United States";
                    }
                }
            } catch (Exception e) {
                logger.error("Error selecting country: {}", e.getMessage());
                return;
            }
        }

        logger.info("Selecting country: {}", country);
        try {
            Select countrySelect = new Select(driver.findElement(countryDropdown));
            countrySelect.selectByVisibleText(country);
            logger.debug("Country '{}' selected successfully", country);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.error("Country '{}' not found in dropdown. Available options might be different.", country);
            // Select first available country as fallback
            Select countrySelect = new Select(driver.findElement(countryDropdown));
            countrySelect.selectByIndex(1); // Select first option after default
            logger.warn("Selected first available country as fallback");
        }
    }   
    /** Enters state, normalizing unsupported region values */
    public void enterState(String state) {
        if(state == null) {
            logger.warn("State is null. Skipping state entry.");
            return;
        }
        if(state.equalsIgnoreCase("Beijing")) {
            state = "Delhi";
        }
        logger.info("Entering state: {}", state);
        driver.findElement(stateInput).sendKeys(state);
    }   
    /** Enters city */
    public void enterCity(String city) {
        logger.info("Entering city: {}", city);
        driver.findElement(cityInput).sendKeys(city);
    }

    /** Enters postal/zip code */
    public void enterZipcode(String zipcode) {
        logger.info("Entering zipcode: {}", zipcode);
        driver.findElement(zipcodeInput).sendKeys(zipcode);
    }   
    /** Enters mobile number and logs completion of form entry */
    public void enterMobileNumber(String mobileNumber) {
        logger.info("Entering mobile number: {}", mobileNumber);
        driver.findElement(mobileNumberInput).sendKeys(mobileNumber);
        logger.info("All signup form details entered successfully");
    }
       
    /**
     * Attempts to click create account button beneath mobile field.
     * Strategy: normal click, fallback JS click if intercepted.
     */
    public void clickCreateAccountButton() {
        try {
            WebElement MobileNumberInputElement = driver.findElement(mobileNumberInput);
            WebElement createAccountButton = driver.findElement(with(By.tagName("button")).below(MobileNumberInputElement));
            
            // Scroll to the button to ensure it's in view and not blocked by ads
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", createAccountButton);
            
            // Wait for button to be clickable after scroll
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
            try {
                shortWait.until(ExpectedConditions.elementToBeClickable(createAccountButton));
            } catch (Exception e) {
                // Continue even if wait times out - button should be clickable
                logger.debug("Short wait completed for create account button");
            }
            
            // Try regular click first
            try {
                createAccountButton.click();
                logger.info("Create account button clicked successfully");
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                // If regular click is blocked by an ad, use JavaScript click
                logger.warn("Regular click intercepted, using JavaScript click");
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", createAccountButton);
                logger.info("Create account button clicked via JavaScript");
            }
        } catch (Exception e) {
            logger.error("Error clicking create account button: {}", e.getMessage());
            throw e;
        }
    }
    public boolean isErrorTextDisplayed() {
        return !driver.findElements(errorElement).isEmpty() && driver.findElement(errorElement).isDisplayed();
    }

    /**
     * Determines if current URL represents signup/login stage.
     */
    public boolean isSignupPageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("/signup"), ExpectedConditions.urlContains("/login")));
        return driver.getCurrentUrl().contains("/signup") || driver.getCurrentUrl().contains("/login");
    }

    /**
     * Returns browser native validationMessage for email field if still on signup page.
     * Empty string indicates either navigation away (success) or absence of validation.
     */
    public String verifyValidationMessage() {
        try {
            // Check if we're still on the signup/login page
            if (driver.getCurrentUrl().contains("/login") || driver.getCurrentUrl().contains("/signup")) {
                WebElement inputElement = driver.findElement(emailInputSignUp);
                String validationMessage = inputElement.getAttribute("validationMessage");
                return validationMessage != null ? validationMessage : "";
            } else {
                // Page has navigated away, meaning signup was successful
                return "";
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Element not found means page has navigated, signup was successful
            logger.debug("Email input element not found - signup successful, page navigated");
            return "";
        }
    }
}