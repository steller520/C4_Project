package selenium.automation.framework.tests.SignUp;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;



import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;
import selenium.automation.framework.utils.ExcelExtractorUtil;

/**
 * REG-03: Data-driven full registration workflow using Excel provider.
 * Flow: Open signup page -> Enter initial name/email -> Validate uniqueness -> Fill profile/address -> Submit.
 * Notes: Early exit on validation failure or existing email. DOB string split (DD-MM-YYYY).
 */
public class TestCase03 extends BaseTest {

    // Scenario: Complete form population and submission.
    
    @Test(dataProvider = "excelDataProvider", dataProviderClass = ExcelExtractorUtil.class)
    public void TC03(@Optional("DefaultTestCase") String testCaseName,String title  , String name, String email, String password, String dateOfBirth, String firstName, String lastName, String company, String address1, String address2, String country, String state, String city, String zipcode, String mobileNumber, String expectedResult) { 

        WebDriver driver = WebdriverUtil.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        // Wait for full document readiness defensively
        wait.until(d -> {
            Object ready = ((JavascriptExecutor) d).executeScript("return document.readyState");
            return ready != null && "complete".equals(ready.toString());
        });

        createTest(testCaseName);
        getTest().info("WebDriver initialized for test: " + testCaseName);
        // Begin workflow for registration
        System.out.println("Executing Test  with testCaseName: " + testCaseName);
        getTest().info("Filling signup form with Name: " + name + ", Email: " + email);
        SignupPage signupPage = new SignupPage(driver);
        signupPage.openSignupPage();
        System.out.println("Opened Signup Page");
        // Fill initial name and email (signup gate)
        signupPage.enterNameForSignup(name);
        System.out.println("Entered Name: " + name);
        signupPage.enterEmailForSignup(email);
        System.out.println("Entered Email: " + email);
        // Click Signup button to expand full registration form
        signupPage.clickSignupButton();
        
        String validationMessage = signupPage.verifyValidationMessage();
        if(validationMessage.isEmpty()) {
            getTest().info("No validation message displayed, proceeding with signup");
        } else {
            getTest().fail("Validation message displayed: " + validationMessage);
            System.out.println("Validation message displayed: " + validationMessage);
            return; // Exit the test if validation fails
        }
        

        if (signupPage.isErrorTextDisplayed()) {
            getTest().fail("Signup failed: Email Address already exist!");
            System.out.println("Signup failed: Email Address already exist!");
            return; // Exit the test if signup cannot proceed
        }

        System.out.println("Clicked Signup Button");
        // Fill extended profile & account details
        // Select title radio by provided value
        getTest().info("Selecting Title: " + title);
        if(title.equalsIgnoreCase("Mr")) {
            signupPage.selectMrRadioButton();
            System.out.println("Selected Title: Mr");
        } else if(title.equalsIgnoreCase("Mrs")) {
            signupPage.selectMrsRadioButton();
            System.out.println("Selected Title: Mrs");
        } else {
            getTest().warning("Invalid title provided: " + title);
        }
        // Enter password and date of birth components
        getTest().info("Entering Password and Date of Birth");
        signupPage.enterPassword(password);
        System.out.println("Entered Password" + password);
        getTest().info("Selecting Date of Birth: " + dateOfBirth);
        String[] dobParts = dateOfBirth.split("-");
        signupPage.selectDateOfBirth(dobParts[0], dobParts[1], dobParts[2]);
        System.out.println("Selected Date of Birth: " + dateOfBirth);

        // Enter personal name details
        getTest().info("Entering First Name: " + firstName + " and Last Name: " + lastName);
        signupPage.enterFirstName(firstName);
        signupPage.enterLastName(lastName);
        System.out.println("Entered First Name: " + firstName + ", Last Name: " + lastName);

        // Enter address & contact information fields
        getTest().info("Entering Company, Address, Country, State, City, Zipcode, Mobile Number");
        signupPage.enterCompanyName(company);
        signupPage.enterAddress1(address1);
        signupPage.enterAddress2(address2);
        
        signupPage.selectCountry(country);
        
        signupPage.enterState(state);
        
        signupPage.enterCity(city);
        signupPage.enterZipcode(zipcode);
        signupPage.enterMobileNumber(mobileNumber);
        System.out.println("Entered Company: " + company + ", Address1: " + address1 + ", Address2: " + address2 + ", Country: " + country + ", State: " + state + ", City: " + city + ", Zipcode: " + zipcode + ", Mobile Number: " + mobileNumber);   
        // Submit registration form
        getTest().info("Submitting the signup form");
        signupPage.clickCreateAccountButton();
        System.out.println("Clicked Create Account Button");
        
        // Expected result verification can be implemented here
        getTest().info("Verifying expected result: " + expectedResult);



        
    }

 

}