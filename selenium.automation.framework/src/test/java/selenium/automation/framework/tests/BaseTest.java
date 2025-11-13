package selenium.automation.framework.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import selenium.automation.framework.utils.SetupExtentReport;
import selenium.automation.framework.utils.WebdriverUtil;

public class BaseTest extends SetupExtentReport {
    protected WebDriver driver;
    

    @BeforeSuite
    public void beforeSuite() {
        // Code to run before the entire test suite
        System.out.println("Starting Test Suite Execution");
        setupReport();
        
    }
    @BeforeMethod
    public void beforeMethod() {
        // Code to run before each test method
       driver = WebdriverUtil.initializeDriver("chrome");
    }
    @AfterMethod
    public void afterMethod() {
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
