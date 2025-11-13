package selenium.automation.framework.tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;

import selenium.automation.framework.utils.SetupExtentReportUtil;
import selenium.automation.framework.utils.WebdriverUtil;

public class BaseTest extends SetupExtentReportUtil {
    protected WebDriver driver;
    

    @BeforeSuite
    public void beforeSuite() {
        // Code to run before the entire test suite
        System.out.println("Starting Test Suite Execution");
        setupReport();
        
    }
    @BeforeMethod
    public void beforeMethod(Method method) {
        // Code to run before each test method
       driver = WebdriverUtil.initializeDriver("chrome");
        // getTest() = extent.
       createTest(method.getName());
        // Log WebDriver initialization info
        getTest().info("WebDriver initialized for test: " + method.getName());    
    }
    @AfterMethod
    public void afterMethod(ITestResult result) {
        ExtentTest test = getTest();
        
         if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        } else {
            test.skip("Test skipped");
        }
        // Code to run after each test method
        WebdriverUtil.quitDriver();
    }

    @AfterSuite
    public void afterSuite() {
        // Code to run after the entire test suite
        System.out.println("Test Suite Execution Completed");
        flushReport();
    }
}
