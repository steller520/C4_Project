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

public class BaseTest extends SetupExtentReportUtil {
    // Removed driver field - use WebdriverUtil.getDriver() instead for thread safety
    

    @BeforeSuite
    public void beforeSuite() {
        // Code to run before the entire test suite
        System.out.println("Starting Test Suite Execution");
        setupReport();

    }


    @BeforeMethod
    @Parameters("browser")
    public void beforeMethod(Method method, @Optional("chrome") String browser) {
        // Code to run before each test method
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
        // Code to run before each class
        System.out.println("");
        
    }

    @AfterClass
    public void afterClass() {
        // Code to run after each class
        System.out.println("");
        WebDriver driver = WebdriverUtil.getDriver();
        if (driver != null) {
            driver.close();
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
        // Quit driver after each test method
        WebdriverUtil.getDriver().close();
    }

    @AfterSuite
    public void afterSuite() {
        // Code to run after the entire test suite
        System.out.println("Test Suite Execution Completed");
        flushReport();
    }
}
