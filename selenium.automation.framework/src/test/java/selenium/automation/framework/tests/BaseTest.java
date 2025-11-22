package selenium.automation.framework.tests;

import java.lang.reflect.Method;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentTest;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.utils.ScreenShotUtil;
import selenium.automation.framework.utils.SetupExtentReportUtil;

/**
 * Abstract base class for TestNG test classes.
 * Provides lifecycle hooks for driver initialization, screenshot capture, and Extent reporting.
 * Design choices:
 *  - ThreadLocal driver obtained via WebdriverUtil for parallel safety.
 *  - Screenshots captured on both PASS and FAIL to aid visual auditing.
 *  - Uses @Parameters browser with system property override (-Dbrowser) for CI flexibility.
 */
@SuppressWarnings("null")
public class BaseTest extends SetupExtentReportUtil {
    // No WebDriver field; access via WebdriverUtil.getDriver() for thread isolation

    @BeforeSuite
    public void beforeSuite() {
        // Suite-wide setup: initialize reporting
        System.out.println("Starting Test Suite Execution");
        setupReport();
    

    }


    @BeforeMethod
    @Parameters("browser")
    public void beforeMethod(Method method, @Optional("chrome") String browser) {
        // Per-test setup: create driver, apply implicit wait baseline
        try {
            // Check system property first (for Maven -Dbrowser=xxx)
            String browserToUse = System.getProperty("browser", browser);
            WebdriverUtil.initializeDriver(browserToUse);
            System.out.println("Browser initialized: " + browserToUse);
            WebdriverUtil.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            // Set waits after driver is created
        } catch (Exception e) {
            System.out.println("Error initializing browser: " + e.getMessage());
        }   
    }

    @BeforeClass
    public void beforeClass() {
        // Placeholder for class-level setup (e.g., shared data fixtures)
        System.out.println("");
        
    }

    @AfterClass
    public void afterClass() {
        // Attempt graceful driver close (not quit) to allow @AfterMethod cleanup patterns
        System.out.println("");
        try {
            WebDriver driver = WebdriverUtil.getDriver();
            if (driver != null) {
                // Check if session is still valid before trying to close
                try {
                    driver.getTitle(); // This will throw exception if session is invalid
                    driver.close();
                } catch (org.openqa.selenium.NoSuchSessionException e) {
                    System.out.println("Browser session already closed");
                } catch (Exception e) {
                    System.out.println("Error checking browser session: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error in afterClass cleanup: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) {
        ExtentTest test = getTest();
        WebDriver driver = WebdriverUtil.getDriver();

        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
            try {
                // Capture screenshot and get filename
                String fileName = ScreenShotUtil.takeScreenshot(driver, method.getName());

                // Create relative path from report location to screenshot
                // Report is in: test-output/reports/
                // Screenshot is in: test-output/screenshots/
                String relativePath = "../screenshots/" + fileName;

                test.addScreenCaptureFromPath(relativePath);
            } catch (Exception e) {
                test.warning("Failed to attach screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
            try {
                // Capture screenshot and get filename
                String fileName = ScreenShotUtil.takeScreenshot(driver, method.getName());

                // Create relative path from report location to screenshot
                // Report is in: test-output/reports/
                // Screenshot is in: test-output/screenshots/
                String relativePath = "../screenshots/" + fileName;

                test.addScreenCaptureFromPath(relativePath);
            } catch (Exception e) {
                test.warning("Failed to attach screenshot: " + e.getMessage());
            }
        } else {
            test.skip("Test skipped");
        }
        // Close window after each test to free resources (quit handled if needed elsewhere)
        try {
            driver.close();
        } catch (Exception e) {
            test.warning("Driver close encountered issue: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Suite teardown: flush report
        System.out.println("Test Suite Execution Completed");
        System.out.println("Flushing Extent Report...");
        flushReport();
        System.out.println("Extent Report flushed successfully!");
    }
}
