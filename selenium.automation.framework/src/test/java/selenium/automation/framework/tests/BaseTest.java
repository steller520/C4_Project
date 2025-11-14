package selenium.automation.framework.tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentTest;

import selenium.automation.framework.core.WebdriverUtil;
import selenium.automation.framework.utils.ScreenShotUtil;
import selenium.automation.framework.utils.SetupExtentReportUtil;

public class BaseTest extends SetupExtentReportUtil {
    protected WebDriver driver;
    WebDriverWait wait;

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
            driver = WebdriverUtil.initializeDriver(browserToUse);
            System.out.println("Browser initialized: " + browserToUse);
        } catch (Exception e) {
            System.out.println("Error initializing browser: " + e.getMessage());
        }

        // getTest() = extent.
        createTest(method.getName());
        // Log WebDriver initialization info
        getTest().info("WebDriver initialized for test: " + method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) {
        ExtentTest test = getTest();

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
        // Code to run after each test method
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebdriverUtil.quitDriver();
    }

    @AfterSuite
    public void afterSuite() {
        // Code to run after the entire test suite
        System.out.println("Test Suite Execution Completed");
        flushReport();
    }
}
