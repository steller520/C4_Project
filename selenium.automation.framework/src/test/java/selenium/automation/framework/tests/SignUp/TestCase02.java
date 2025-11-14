package selenium.automation.framework.tests.SignUp;

import org.testng.annotations.Test;

import selenium.automation.framework.pages.SignupPage;
import selenium.automation.framework.tests.BaseTest;

public class TestCase02 extends BaseTest {

    // Test Case 02: Verify Elements on Signup Page
    @Test
    public void TC02() {
        System.out.println("Executing Test Case 02");
        SignupPage signupPage = new SignupPage(driver);
        boolean isNameInputDisplayed = signupPage.isNameInputSignUpDisplayed();
        boolean isEmailInputDisplayed = signupPage.isEmailInputSignUpDisplayed();
        boolean isSignupButtonDisplayed = signupPage.isSignupButtonDisplayed();
        getTest().info("Verifying elements(Name,Email,Signup Button) on Signup Page", null);
        if(isNameInputDisplayed) {
            getTest().pass("Name input field for signup is displayed.");
        } else {
            getTest().fail("Name input field for signup is NOT displayed.");
        }

        if(isEmailInputDisplayed) {
            getTest().pass("Email input field for signup is displayed.");
        } else {
            getTest().fail("Email input field for signup is NOT displayed.");
        }

        if(isSignupButtonDisplayed) {
            getTest().pass("Signup button is displayed.");
        } else {
            getTest().fail("Signup button is NOT displayed.");
        }
        
        
    }
}
