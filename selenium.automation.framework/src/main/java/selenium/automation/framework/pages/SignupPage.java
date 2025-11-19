package selenium.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import selenium.automation.framework.core.ConfigManager;

public class SignupPage {
// WebDriver instance
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


    

    // Constructor to initialize WebDriver
    public SignupPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to open the Signup page
    public void openSignupPage() {
        String signupPageUrl = ConfigManager.getProperty("SignUpPageURL");
        driver.get(signupPageUrl);
        driver.manage().window().maximize();
    }

    // Method to check if name input for signup is displayed
    public boolean isNameInputSignUpDisplayed() {
        return driver.findElement(nameInputSignUp).isDisplayed();
    }

    // Method to enter name and email for signup
    public void enterNameForSignup(String name) {
        driver.findElement(nameInputSignUp).sendKeys(name);
    }

    // Method to check if email input for signup is displayed
    public boolean isEmailInputSignUpDisplayed() {
        return driver.findElement(emailInputSignUp).isDisplayed();
    }

    // Method to enter email for signup
    public void enterEmailForSignup(String email) {
        driver.findElement(emailInputSignUp).sendKeys(email);
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

    // Method to click on signup button
    public void clickSignupButton() {
        driver.findElement(signupButton).click();

    }
    
    // Method to select Mr. radio button
    public void selectMrRadioButton() {
        driver.findElement(radioButtonForMr).click();
    }

    // Method to select Mrs. radio button
    public void selectMrsRadioButton() {
        driver.findElement(radioButtonForMrs).click();
    }
    // Method to enter password
    public void enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    // Method to select date of birth (expects year, month, day format)
    public void selectDateOfBirth(String year, String month, String day) {
        Select daySelect = new Select(driver.findElement(daysDropdown));
        String[] dayParts = day.split("");
        if(dayParts[0].equals("0")) {
            day = dayParts[1];
        }
        daySelect.selectByValue(day);
        Select monthSelect = new Select(driver.findElement(monthsDropdown));
        monthSelect.selectByIndex(Integer.parseInt(month));
        Select yearSelect = new Select(driver.findElement(yearsDropdown));
        yearSelect.selectByVisibleText(year);
    }
    // Method to enter first name
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameInput).sendKeys(firstName);
    }
    // Method to enter last name
    public void enterLastName(String lastName) {
        driver.findElement(lastNameInput).sendKeys(lastName);
    }
    // Method to enter company name
    public void enterCompanyName(String company) {
        driver.findElement(companyInput).sendKeys(company);
    }
    // Method to enter address1
    public void enterAddress1(String address1) {
        driver.findElement(address1Input).sendKeys(address1);
    }  
    // Method to enter address2
    public void enterAddress2(String address2) {
        driver.findElement(address2Input).sendKeys(address2);
    }   
    // Method to select country
    public void selectCountry(String country) {
        Select countrySelect = new Select(driver.findElement(countryDropdown));
        countrySelect.selectByVisibleText(country);
    }   
    // Method to enter state
    public void enterState(String state) {
        driver.findElement(stateInput).sendKeys(state);
    }   
    // Method to enter city
    public void enterCity(String city) {
        driver.findElement(cityInput).sendKeys(city);
    }

    // Method to enter zipcode
    public void enterZipcode(String zipcode) {
        driver.findElement(zipcodeInput).sendKeys(zipcode);
    }   
    // Method to enter mobile number
    public void enterMobileNumber(String mobileNumber) {
        driver.findElement(mobileNumberInput).sendKeys(mobileNumber);
    }   
    // Method to click on create account button
    public void clickCreateAccountButton() {
        WebElement MobileNumberInputElement = driver.findElement(mobileNumberInput);
        WebElement createAccountButton = driver.findElement(with(By.tagName("button")).below(MobileNumberInputElement));
        createAccountButton.click();
    }
}