package selenium.automation.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReports setup and thread-safe test logger accessor.
 * Manages single report instance per suite and per-test ExtentTest via ThreadLocal
 * to support parallel execution.
 */
public class SetupExtentReportUtil {

    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    // Thread-safe storage of current test
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Initializes ExtentReports with timestamped HTML report.
     * Should be invoked once in suite setup (e.g., @BeforeSuite).
     */
    public static void setupReport() {
        System.out.println("Initializing Extent Report...");

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/test-output/reports/ExtentReport_" + timestamp + ".html";

        sparkReporter = new ExtentSparkReporter(reportPath);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Tester", "Shivam Negi");
        sparkReporter.config().setReportName("Selenium Automation Test Report");
        sparkReporter.config().setDocumentTitle("Selenium Automation Test Report");

        System.out.println("Extent Report setup completed: " + reportPath);
    }

    /**
     * Creates and registers a new ExtentTest for current thread.
     * @param testName logical test identifier
     * @return created ExtentTest
     */
    public static ExtentTest createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTest.set(test);
        return test;
    }

    /**
     * Retrieves thread-bound ExtentTest instance.
     * @return current ExtentTest or null if not yet created
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Flushes report buffers to disk; safe no-op if not initialized.
     */
    public static void flushReport() {
        System.out.println("Flushing Extent Report...");
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
