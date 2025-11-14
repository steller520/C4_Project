package selenium.automation.framework.tests.SignUp;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase03 extends BaseTest {

    // Test Case 03: Verify complete signup form is visible
    
    @Test
    @Parameters({"name", "email"})
    public void TC03(@Optional("DefaultName") String name, @Optional("default@email.com") String email) {
        // Implementation for Test Case 03
        System.out.println("Executing Test Case 03");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.enterNameForSignup(name);
        signupPage.enterEmailForSignup(email);
        signupPage.clickSignupButton();
    }
}
