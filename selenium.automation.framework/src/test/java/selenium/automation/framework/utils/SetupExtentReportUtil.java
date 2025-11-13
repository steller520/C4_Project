package selenium.automation.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetupExtentReportUtil {

    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    // Thread-safe storage of current test
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Setup report once per suite
    public static void setupReport() {
        System.out.println("Initializing Extent Report...");

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/test-output/reports/ExtentReport_" + timestamp + ".html";

        sparkReporter = new ExtentSparkReporter(reportPath);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Tester", "Shivam Negi");

        System.out.println("Extent Report setup completed: " + reportPath);
    }

    // Create ExtentTest per test method
    public static ExtentTest createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTest.set(test);
        return test;
    }

    // Return current test thread-safe
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    // Flush at end of suite
    public static void flushReport() {
        System.out.println("Flushing Extent Report...");
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
