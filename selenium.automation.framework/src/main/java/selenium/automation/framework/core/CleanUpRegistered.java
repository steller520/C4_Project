package selenium.automation.framework.core;

import org.openqa.selenium.WebDriver;

import selenium.automation.framework.pages.HomePage;
import selenium.automation.framework.pages.LoginPage;

/**
 * Utility class intended to perform post-test cleanup of any registered test users.
 *
 * <p>NOTE: This implementation currently relies on UI interactions. For large test
 * suites or production systems, prefer using a backend API or database operation
 * for faster and more reliable cleanup.</p>
 */
public class CleanUpRegistered {
    /**
     * Simple manual runner to invoke cleanup from the command line.
     * In CI this would normally not be used; instead call {@link #cleanUp(WebDriver)} after suites.
     */
    public static void main(String[] args) {
        // Initialize a throwaway Chrome session for cleanup
        WebDriver driver = WebdriverUtil.initializeDriver("chrome");
        cleanUp(driver); // Execute cleanup logic
        WebdriverUtil.quitDriver(); // Ensure browser is closed
    }

    /**
     * Iterates through predefined user credentials and attempts to log in / delete each account.
     *
     * @param driver active WebDriver instance
     */
    public static void cleanUp(WebDriver driver) {
        System.out.println("Cleaning up registered users.");
        // Instantiate page objects required for navigation.
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Iterate over the static data set of test users.
        for (Object[] userData : data) {
            String email = (String) userData[0];
            String password = (String) userData[1];

            System.out.println("Attempting cleanup for user: " + email);

            // Navigate to login and attempt authentication.
            loginPage.openLoginPage();
            loginPage.enterLoginEmail(email);
            loginPage.enterLoginPassword(password);
            loginPage.clickLoginButton();

            // If login fails, skip this user (may already be deleted or invalid).
            boolean isError = loginPage.isErrorTextDisplayed();
            if (isError) {
                System.out.println("Login failed for user: " + email + ". Skipping deletion.");
                continue;
            } else {
                // User logged in successfully – attempt account deletion.
                // Depending on application flow, this may navigate away or require confirmation.
                homePage.clickDeleteAccount();
                System.out.println("Requested deletion for user: " + email);
            }
        }

        // End of cleanup loop. Add verification logic if needed (e.g., re-login checks).
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


    /**
     * Static list of test users provisioned during sign-up scenarios.
     * Add / remove entries as test coverage evolves. Duplicate entries (same email) will
     * naturally fail if the password does not match current state.
     */
    static Object [][] data = {
        {"john.doe+test1@example.com", "Passw0rd!"},
        {"priya.sharma@example.co.in" , "P@ss1234"},
        {"longname.user@example.com", "LongPass123!"},
        {"notitleuser@example.com", "pwd"},
        
        {"unicode.user@example.com", "Un1c0de!"},
        {"sql.injection@example.com", "P@ssword1"},
        
        {"test.user+signup@example.com", "Complex#1234"},
        {"john.doe+test1@example.com", "AnotherPass1!"} // Duplicate email used with alternate password
    };

}


