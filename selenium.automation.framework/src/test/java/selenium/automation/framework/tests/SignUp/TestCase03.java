package selenium.automation.framework.tests.SignUp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;
import selenium.automation.framework.utils.ExcelExtractorUtil;

public class TestCase03 extends BaseTest {

    // Test Case 03: Verify complete signup form is visible
    
    @Test(dataProvider = "excelDataProvider", dataProviderClass = ExcelExtractorUtil.class)
    public void TC03(@Optional("DefaultTestCase") String testCaseName,String title  , String name, String email, String password, String dateOfBirth, String firstName, String lastName, String company, String address1, String address2, String country, String state, String city, String zipcode, String mobileNumber, String expectedResult) { 

        WebDriver driver = WebdriverUtil.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(d -> ((org.openqa.selenium.JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

        createTest(testCaseName);
        getTest().info("WebDriver initialized for test: " + testCaseName);
        // Implementation for Test Case 03
        System.out.println("Executing Test  with testCaseName: " + testCaseName);
        getTest().info("Filling signup form with Name: " + name + ", Email: " + email);
        SignupPage signupPage = new SignupPage(driver);
        signupPage.openSignupPage();
        System.out.println("Opened Signup Page");
        // Fill name and email
        signupPage.enterNameForSignup(name);
        System.out.println("Entered Name: " + name);
        signupPage.enterEmailForSignup(email);
        System.out.println("Entered Email: " + email);
        // Click Signup button
        signupPage.clickSignupButton();
        System.out.println("Clicked Signup Button");
        // Fill additional details
        // Select title
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
        // Enter password and date of birth
        getTest().info("Entering Password and Date of Birth");
        signupPage.enterPassword(password);
        System.out.println("Entered Password" + password);
        getTest().info("Selecting Date of Birth: " + dateOfBirth);
        String[] dobParts = dateOfBirth.split("-");
        signupPage.selectDateOfBirth(dobParts[0], dobParts[1], dobParts[2]);
        System.out.println("Selected Date of Birth: " + dateOfBirth);

        // Enter first name and last name
        getTest().info("Entering First Name: " + firstName + " and Last Name: " + lastName);
        signupPage.enterFirstName(firstName);
        signupPage.enterLastName(lastName);
        System.out.println("Entered First Name: " + firstName + ", Last Name: " + lastName);

        // Enter company, address, country, state, city, zipcode, mobile number
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
        // Submit the form and verify expected result
        getTest().info("Submitting the signup form");
        signupPage.clickCreateAccountButton();
        System.out.println("Clicked Create Account Button");
        
        // Expected result verification can be implemented here
        getTest().info("Verifying expected result: " + expectedResult);



        
    }

 

}