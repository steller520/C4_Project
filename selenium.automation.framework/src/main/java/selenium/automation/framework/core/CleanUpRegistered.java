package selenium.automation.framework.core;

import org.openqa.selenium.WebDriver;

import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.pages.LoginPage;

public class CleanUpRegistered {
    public static void main(String[] args) {
        // Example usage
        WebDriver driver = WebdriverUtil.initializeDriver("chrome");
        cleanUp(driver);
        WebdriverUtil.quitDriver();
    }

    public static void cleanUp(WebDriver driver) {
        // Placeholder for cleanup logic after registration tests
        System.out.println("Cleaning up registered users.");
        // Implement actual cleanup logic here, e.g., deleting test users from the application
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        // Add login and deletion logic as needed
        for (Object[] userData : data) {
            String email = (String) userData[0];
            String password = (String) userData[1];
            // Perform login and deletion using email and password
            System.out.println("Cleaning up user: " + email);
            loginPage.openLoginPage();
            loginPage.enterLoginEmail(email);
            loginPage.enterLoginPassword(password);
            loginPage.clickLoginButton();

            boolean isError = loginPage.isErrorTextDisplayed();
            if (isError) {
                System.out.println("Login failed for user: " + email + ". Skipping deletion.");
                continue;
            }else{

                // After login, navigate to account settings or admin panel to delete the user
                homePage.clickDeleteAccount();
                // Add deletion steps here
            }
            
        }

        

    }

//     john.doe+test1@example.com	Passw0rd!
// priya.sharma@example.co.in	P@ss1234
// longname.user@example.com	LongPass123!
// notitleuser@example.com	pwd
// invalid-email-format	ValidPass1!
// unicode.user@example.com	Un1c0de!
// sql.injection@example.com	P@ssword1
// 	SomePass1!
// test.user+signup@example.com	Complex#1234
// john.doe+test1@example.com	AnotherPass1!


    static Object [][] data = {
        {"john.doe+test1@example.com", "Passw0rd!"},
        {"priya.sharma@example.co.in" , "P@ss1234"},
        {"longname.user@example.com", "LongPass123!"},
        {"notitleuser@example.com", "pwd"},
        
        {"unicode.user@example.com", "Un1c0de!"},
        {"sql.injection@example.com", "P@ssword1"},
        
        {"test.user+signup@example.com", "Complex#1234"},
        {"john.doe+test1@example.com", "AnotherPass1!"}
    };

}


